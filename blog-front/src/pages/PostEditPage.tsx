import React, { useCallback, useEffect, useRef, useState } from "react";
import Keycloak from "keycloak-js";
import ReactQuill from "react-quill-new";
import 'react-quill-new/dist/quill.snow.css';
import TestHeader from "../components/header/TestHeader";
import { useNavigation } from "../util/navigation";
import { ROUTES } from "../constants/routes";
import '../styles/quillStyle.css'
import { API_IMAGE_PRESIGNED_URL, API_POST_CREATE_URL, API_POST_URL, CLOUD_FRONT_URL } from "../util/apiUrl";
import axios from "axios";
import { createPost } from "../service/postService";

interface Props{
    keycloak: Keycloak | null;
}

const PostEditPage: React.FC<Props> = ({keycloak}) => {
    const [userInfo, setUserInfo] = useState<null | Record<string, any>>(null);
    const [title, setTitle] = useState<string>(''); 
    const titleRef = useRef<HTMLTextAreaElement>(null);
    const [content, setContent] = useState<string>('');
    const quillRef = useRef<ReactQuill>(null);

    const { navigateTo } = useNavigation();

    useEffect(() => {
        // 키클락 객체상태 분기점
        console.log(`keycloak 객체 상태 : ${keycloak}`)
        if (keycloak && keycloak.authenticated) {
            // 사용자 정보 로드
            keycloak.loadUserInfo().then((userInfo) => {
                setUserInfo(userInfo);
                console.log(JSON.stringify(userInfo));
                
            });
        } else {
            keycloak?.login();
        }
    }, [keycloak]);


    // ---------------------------------------------------------
    const handleTitleChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
        setTitle(e.target.value);
        autoResizeTitle();
    };
    const autoResizeTitle = () => {
        if (titleRef.current) {
            titleRef.current.style.height = 'auto'; // 초기화
            titleRef.current.style.height = `${titleRef.current.scrollHeight}px`; // 스크롤 높이로 설정
        }
    };
    // ---------------------------------------------------------

    const imageHandler = useCallback(async ()=> {
        const input = document.createElement('input');
        input.setAttribute('type', 'file');
        input.setAttribute('accept', 'image/*');
        input.click();
    
        input.onchange = async () => {
          const file = input.files?.[0];
          if (file) {
            try {
              const token = keycloak?.token;
              console.log("1. Presigned URL 요청 시작");
              // presigned URL 요청
              const response = await axios.post(
                API_IMAGE_PRESIGNED_URL, 
                {
                    fileName: file.name,
                    fileType: file.type,
                },
                { headers: { Authorization: `Bearer ${token}` }}
            ).then((response) => {
                console.log("2. Presigned URL 요청 성공", response.data);
                return response;
            }).catch((error) => {
                console.error("2. Presigned URL 요청 실패", error.response ? error.response.data : error.message);
                throw error;
            });
              
              const { presignedUrl, objectKey } = response.data;
              console.log("3. Presigned URL로 이미지 업로드 시작", { presignedUrl, objectKey });
              
              // presigned URL로 이미지 업로드
              await axios.put(presignedUrl, file, {
                headers: {
                    'Content-Type': file.type,
                },
            }).then((response) => {
                console.log("4. Presigned URL로 이미지 업로드 성공", response.status);
            }).catch((error) => {
                console.error("4. Presigned URL로 이미지 업로드 실패", error.response ? error.response.data : error.message);
                throw error;
            });

              const imageUrl = CLOUD_FRONT_URL +`${objectKey}`;
              console.log("5. 최종 이미지 URL:", imageUrl);
              // 에디터에 이미지 삽입
              const quill = quillRef.current?.getEditor();
              if (!quill) {
                console.error("Quill editor is not initialized.");
                return;
              }
              const range = quill?.getSelection();
    
              if (!range) {
                quill.focus();
                const newRange = quill.getSelection();
                if (!newRange) {
                  return;
                }
                quill.insertEmbed(newRange.index, 'image', imageUrl);
              } else{
                quill.insertEmbed(range.index, 'image', imageUrl);
                console.log("6.이미지 삽입 최종 성공");
              }
            } catch (error) {
              console.error("이미지 업로드 중 에러 발생:", error);
            }
          }
        };
      },[keycloak]);

    const quillModules = {
        toolbar: {
          container: [
            ["image"],
            [{ header: [1, 2, 3, 4, 5, false] }],
            ["bold", "underline"],
          ],
          handlers: {
            image: imageHandler,
          },
        },
      };

    // ---------------------------------------------------------
    const handleSubmit = async () => {
        console.log("Title:", title);
        console.log("Content:", content);
        const token = keycloak?.token;
        const nickname = userInfo?.nickname;

        if (!token || !nickname) {
            console.error("Token or nickname is missing");
            return;
          }
        try {
            await createPost(
              {
                nickname: nickname,
                title: title,
                content: content,
              },
              token
            );
      
            console.log("Post created successfully");
            // 성공 시 최종적일관성을위해 결과페이지로
          } catch (error) {
            console.error("Error creating post:", error);
          }
    };

    // ---------------------------------------------------------
    if(!keycloak?.authenticated) {
        // 로그인 상태가 확정이지 않을때
        return (
            <div>
                로딩중 ...
            </div>
        )
    }

    return (
        <div className="flex flex-col h-full overflow-hidden ">
            {/* 헤더 */}
            <TestHeader keycloak={keycloak}/>
            {/* 본문 */}
            <div className="flex-1 flex flex-col min-h-0 gap-2 pt-4 max-w-screen-2xl mx-auto w-full shadow-custom-default">
                {/* 타이틀 */}
                <div className="">
                    <textarea
                        ref={titleRef}
                        value={title}
                        onChange={handleTitleChange}
                        placeholder="제목을 입력하세요"
                        className="p-3 font-bold text-5xl w-full focus:outline-none outline-none"
                        rows={1}
                    />
                </div>
                {/* 콘탠트 */}
                <ReactQuill
                    ref={quillRef}
                    value={content}
                    onChange={setContent}
                    placeholder="블로그 내용을 작성하세요..."
                    className="react-quill"
                    modules={quillModules}
                />
                {/* 하위 버튼 */}
                <div className='flex flex-row justify-between bg-white items-center mb-1 shadow-custom-default'>
                    <button 
                        className='px-8 py-4 font-bold text-black rounded-md cursor-pointer hover:bg-red-700 hover:text-white'
                    >
                        ← 나가기
                    </button>
                    <button 
                        className="px-8 py-4 font-bold bg-blue-500 text-white rounded cursor-pointer hover:bg-blue-600"
                        onClick={handleSubmit}
                    >
                        작성하기
                    </button>
                </div>  
            </div>
        </div>
    );
}

export default PostEditPage;
