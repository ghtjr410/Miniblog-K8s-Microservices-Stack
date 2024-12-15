import React, { useEffect, useState } from "react"; 
import Keycloak from "keycloak-js";
import { useLocation, useParams } from "react-router-dom";
import useNavigationHelper from "../util/navigationUtil";
import TestHeader from "../components/header/TestHeader";
import { createComment } from "../service/commentService";
import { getPostDetail } from "../service/queryService.public";
import { formatDate } from "../util/dateUtil";
import DOMPurify from "dompurify";

interface Props{
    keycloak: Keycloak | null;
}

// 댓글 인터페이스 정의
interface CommentData {
    commentUuid: string;
    postUuid: string;
    userUuid: string;
    nickname: string;
    content: string;
    createdDate: string;
    updatedDate: string;
}

interface PostDetailData{
    commentCount: number;
    content: string;
    createdDate: string;
    likeCount: number;
    nickname: string;
    postUuid: string;
    title: string;
    updatedDate: string;
    userUuid: string;
    viewcount: number;
}

const initialPostDetailData: PostDetailData = {
    commentCount: 0,
    content: "",
    createdDate: "",
    likeCount: 0,
    nickname: "",
    postUuid: "",
    title: "",
    updatedDate: "",
    userUuid: "",
    viewcount: 0,
};


const PostDetailPage: React.FC<Props> = ({ keycloak }) => {
    const { nickname, postUuid} = useParams();
    const { toHome } = useNavigationHelper();
    const [postDetailData, setPostDetailData] = useState<PostDetailData>(initialPostDetailData);
    const [comments, setComments] = useState<CommentData[]>([]);
    const [content, setContent] = useState<string>('');
    const [isLogin, setIsLogin] = useState<boolean>(false);
    const [userInfo, setUserInfo] = useState<null | Record<string, any>>(null);

    const [buttonPosition, setButtonPosition] = useState("50%");

    const handleScroll = () => {
        const scrollY = window.scrollY;

        // 스크롤이 200px을 넘으면 버튼을 위쪽에 고정
        if (scrollY > 200) {
            setButtonPosition("20%");
        } else {
            setButtonPosition("50%");
        }
    };

    const handleTitleChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
        setContent(e.target.value);
    }

    const handleCommentSumit = async () => {
        if (!isLogin) {
            alert("로그인 이용자만 사용가능");
            return;
        }

        const token = keycloak?.token;
        const nickname = userInfo?.nickname;


        if (!token || !nickname) {
            console.error("Token or nickname is missing");
            return;
        }

        createComment(
            {
                postUuid: postDetailData.postUuid,
                nickname: nickname,
                content: content,
            },
            token
        )
        .then((response) => {
            console.log("Comment created successfully:", response);
            console.log(`Nickname: ${response.nickname}, Content: ${response.content}`);
            console.log({
                commentUuid: response.commentUuid, // 응답에서 반환된 UUID
                postUuid: postDetailData.postUuid,
                userUuid: userInfo?.sub, // Keycloak에서 userUuid를 가져옵니다.
                nickname: response.nickname,
                content: response.content,
                createdDate: new Date().toISOString(),
                updatedDate: new Date().toISOString(),
            });
            // 성공 시 댓글 목록을 갱신하거나 입력 필드를 초기화
            setContent(''); // 입력 필드 초기화
            // 여기서 댓글창을 하나 임의로 만들어야돼 그실험으로 
            setComments((prevComments) => [
                ...prevComments,
                {
                    commentUuid: response.commentUuid, // 응답에서 반환된 UUID
                    postUuid: response.postUuid,
                    userUuid: userInfo?.sub, // Keycloak에서 userUuid를 가져옵니다.
                    nickname: response.nickname,
                    content: response.content,
                    createdDate: response.createdDate,
                    updatedDate: response.updatedDate,
                },
            ]);
        })
        .catch((error) => {
            console.error("Error creating comment:", error);
        });
    }

    const fetchPostDetail = (postUuid: string) => {
        getPostDetail(postUuid)
        .then((response) => {
            console.log(response);
            setPostDetailData(response.post);
            setComments(response.comments);
        })
        .catch((error) => {
            console.error("데이터가져오는데 실패", error);
        })
    }

    

    useEffect(() => {
        if (postUuid) {
            fetchPostDetail(postUuid);
        } else {
            alert("해당 게시글을 찾을 수 없습니다.");
            toHome();
        }

        console.log(postDetailData);
        window.addEventListener("scroll", handleScroll);

        return () => {
            window.removeEventListener("scroll", handleScroll);
        };
    }, [postUuid]);

    useEffect(() => {
        // 키클락 객체상태 분기점
        console.log(`keycloak 객체 상태 : ${keycloak}`)
        if (keycloak && keycloak.authenticated) {
            // 사용자 정보 로드
            keycloak.loadUserInfo().then((userInfo) => {
                setUserInfo(userInfo);
                console.log(JSON.stringify(userInfo));
                setIsLogin(true);
            });
        } else {
            setIsLogin(false);
        }
    }, [keycloak]);


    if (!postUuid) {
        return (
            <div className="flex flex-col h-full">
                <TestHeader keycloak={keycloak} nickname={nickname}/>
                <div>로딩 중.. (스켈레톤 UI)</div>
            </div>
    );
    }
    return (
        <div className="flex flex-col ">
            <TestHeader keycloak={keycloak} nickname={nickname} />
            <div className="flex-1 flex flex-col min-h-0 gap-2 pt-4 max-w-screen-md mx-auto w-full shadow-custom-default px-3">
                {/* 제목 */}
                <div className="mt-20 text-5xl font-bold bg-slate-100 leading-snug">
                    {postDetailData.title}
                </div>
                {/* 게시글 정보 */}
                <div className="flex mt-8 justify-between">
                    <div className="flex text-base gap-2">
                        <div className="font-bold">
                            {postDetailData.nickname}
                        </div>
                        <div>·</div>
                        <div>
                            {formatDate(postDetailData.createdDate)}
                        </div>
                    </div>
                    {postDetailData.userUuid === userInfo?.sub &&(
                        <div className="flex gap-2 text-gray-500">
                            <div className="cursor-pointer hover:text-black">수정</div>
                            <div className="cursor-pointer hover:text-black">삭제</div>
                        </div>
                    )}
                </div>
                {/* 내용 */}
                <div 
                    className="mt-4"
                    dangerouslySetInnerHTML={{ __html: DOMPurify.sanitize(postDetailData.content) }}
                >
                    {/* {postDetailData.content} */}
                </div>
                {/* 댓글 작성 */}
                <div className="bg-slate-100 flex-col border-t">
                    <div className="mt-2 font-bold text-lg">
                        {postDetailData.commentCount}개의 댓글
                    </div>
                    <textarea
                        className="p-3 mt-2 w-full focus:outline-none border border-gray-300 rounded-md resize-none"
                        onChange={handleTitleChange}
                        placeholder="댓글을 입력하세요"
                        style={{ minHeight: "100px", overflow: "hidden" }}
                        onInput={(e) => {
                            const target = e.target as HTMLTextAreaElement;
                            target.style.height = "auto"; // 높이 초기화
                            target.style.height = `${target.scrollHeight}px`; // 높이를 내용에 맞게 조정
                        }}
                    />
                    <div className="mt-4 flex justify-end">
                        <button 
                            className="bg-blue-500 text-white px-6 py-2 rounded cursor-pointer hover:bg-blue-600"
                            onClick={handleCommentSumit}
                        >
                            댓글 작성
                        </button>
                    </div>
                </div>
                {/* 댓글 리스트 */}
                <div className="mt-4 bg-slate-200 flex flex-col">
                {comments.length > 0 ? (
                    comments.map((comment) => (
                        <div key={comment.commentUuid} className="py-6 bg-slate-300 flex flex-col border-t border-black">
                            <div className="flex justify-between">
                                <div>
                                    <div className="text-lg font-bold">{comment.nickname}</div>
                                    <div className="text-gray-500">{formatDate(comment.createdDate)}</div>
                                </div>
                                {comment.userUuid === userInfo?.sub && (
                                    <div className="flex gap-2 text-gray-500">
                                        <div className="cursor-pointer hover:text-black">수정</div>
                                        <div className="cursor-pointer hover:text-black">삭제</div>
                                    </div>
                                )}
                            </div>
                            <div className="mt-4 leading-loose">{comment.content}</div>
                        </div>
                    ))
                ) : (
                    <div className="py-6 text-center text-gray-500">댓글이 없습니다.</div>
                )}
                </div>
            </div>
            {/* ♥ 버튼 */}
            <button
                className="fixed right-8 p-4 rounded-full shadow-lg bg-gray-100"
                style={{ 
                    top: buttonPosition, 
                    right: "calc(50% - 500px)", // 본문 너비에 맞춘 오른쪽 여백 설정
                    transition: "top 0.3s ease" }}
            >
                <div className="w-10 h-10 flex items-center justify-center rounded-full bg-red-500 hover:bg-red-600 text-white">
                    ♥
                </div>
                <div>{postDetailData.likeCount}</div>
            </button>
        </div>
    );
}

export default PostDetailPage;



