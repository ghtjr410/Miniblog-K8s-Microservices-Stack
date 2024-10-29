import React, { useEffect, useRef, useState } from "react";
import Keycloak from "keycloak-js";
import ReactQuill from "react-quill-new";
import 'react-quill-new/dist/quill.snow.css';
import TestHeader from "../components/header/TestHeader";
import { useNavigation } from "../util/navigation";
import { ROUTES } from "../constants/routes";
import '../styles/quillStyle.css'

interface Props{
    keycloak: Keycloak | null;
}

const PostEditPage: React.FC<Props> = ({keycloak}) => {
    const [title, setTitle] = useState<string>('');
    const titleRef = useRef<HTMLTextAreaElement>(null);

    const [content, setContent] = useState<string>('');
    const quillRef = useRef<ReactQuill>(null);

    const { navigateTo } = useNavigation();

    // ---------------------------------------------------------
    useEffect(() => {
        // 키클락 객체상태 분기점
        console.log(`keycloak 객체 상태 : ${keycloak}`)
        if (!keycloak?.authenticated) {
            keycloak?.login();
        }
    })
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
    const quillModules = {
        toolbar: {
          container: [
            ["image"],
            [{ header: [1, 2, 3, 4, 5, false] }],
            ["bold", "underline"],
          ],
        //   handlers: {
        //     image: imageHandler,
        //   },
    
        },
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
                    >
                        작성하기
                    </button>
                </div>  
            </div>
        </div>
    );
}

export default PostEditPage;