import React, { useEffect, useRef, useState } from "react"; 
import Keycloak from "keycloak-js";
import { useLocation, useParams } from "react-router-dom";
import useNavigationHelper from "../util/navigationUtil";
import TestHeader from "../components/header/TestHeader";
import { createComment, deleteComment, updateComment } from "../service/commentService";
import { getPostDetail } from "../service/queryService.public";
import { formatDate } from "../util/dateUtil";
import DOMPurify from "dompurify";
import { deletePost } from "../service/postService";
import { incrementViewcount } from "../service/viewcountService";
import { getUserLikedPosts } from "../service/queryService.auth";
import { toggleLike } from "../service/likeService";
import { FaHeart } from "react-icons/fa";
import { BsList, BsX, BsXCircle } from "react-icons/bs";

interface Props{
    keycloak: Keycloak | null;
    keycloakStatus: "loading" | "authenticated" | "unauthenticated";
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
    postUuid: string;
    userUuid: string;
    nickname: string;
    title: string;
    content: string;
    createdDate: string;
    updatedDate: string;
    viewcount: number;
    likeCount: number;
    commentCount: number;
}

const initialPostDetailData: PostDetailData = {
    postUuid: "",
    userUuid: "",
    nickname: "",
    title: "",
    content: "",    
    createdDate: "",
    updatedDate: "",
    viewcount: 0,
    likeCount: 0,
    commentCount: 0,
};


const PostDetailPage: React.FC<Props> = ({ keycloak, keycloakStatus }) => {
    const { nickname, postUuid} = useParams();
    const [kecloakLoading, isKecloakLoading] = useState<boolean>(true);
    const [isUserInfoLoaded, setIsUserInfoLoaded] = useState<boolean>(false);
    const [userInfo, setUserInfo] = useState<null | Record<string, any>>(null);
    const [postDetailData, setPostDetailData] = useState<PostDetailData>(initialPostDetailData);
    const [content, setContent] = useState<string>('');
    const [comments, setComments] = useState<CommentData[]>([]);
    const [isLike, setIsLike] = useState<boolean>(false);
    const [likeCount, setLikeCount] = useState<number>(0);

    const [editCommentId, setEditCommentId] = useState<string | null>(null);
    const [editUserId, setEditUserId] = useState<string | null>(null);
    const [editContent, setEditContent] = useState<string>("");

    const textareaRef = useRef<HTMLTextAreaElement>(null);
    const { toHome, toPostRewrite } = useNavigationHelper();
    const location = useLocation();

    const [likeBtnPosition, setLikeBtnPosition] = useState("50%");

    const [isSideVisible, setIsSideVisible] = useState<boolean>(true);
    const [isMobileMenuOpen, setIsMobileMenuOpen] = useState<boolean>(false);

    // 1번
    useEffect(() => {
        // 키클락 객체상태 분기점
        console.log(`keycloak 객체 상태 : ${keycloak}`)
        if (keycloakStatus !== "loading") {
            if(keycloakStatus === "unauthenticated") {
                console.log(`@@@ ${keycloakStatus}`)
            }
            if(keycloakStatus === "authenticated") {
                console.log(`@@@ ${keycloakStatus}`)
                isKecloakLoading(false);
            }
            
            
        }
        if (keycloak && keycloak.authenticated) {
            // 사용자 정보 로드
            keycloak.loadUserInfo().then((userInfo) => {
                setUserInfo(userInfo);
                setIsUserInfoLoaded(true);
                console.log(JSON.stringify(userInfo));
            });
        }
    }, [keycloak, location.state]);

    // 2번
    useEffect(() => {
        console.log(`key클락 상태는?? : ${keycloakStatus} : ${kecloakLoading}`);
        if(kecloakLoading) return;
        // location.state에서 사전 읽기 데이터를 확인
        if (location.state?.postDetailData) {
            setPostDetailData(location.state.postDetailData);
        } else if (postUuid) {
            // 사전 읽기 데이터가 없으면 서버에서 데이터 요청
            fetchPostDetail(postUuid);
            handleIncrementViewcount(postUuid);

        } else {
            alert("해당 게시글을 찾을 수 없습니다.");
            toHome();
        }

        console.log(postDetailData);
        window.addEventListener("scroll", handleScroll);

        return () => {
            window.removeEventListener("scroll", handleScroll);
        };
    }, [postUuid, kecloakLoading]);

    useEffect(() => {
        console.log(`isUserInfoLoaded 상태 : ${isUserInfoLoaded}`);
        if (isUserInfoLoaded) {
            fetchLikeList();
        }
    }, [isUserInfoLoaded]);

    useEffect(() => {
        // 브라우저 너비에 따른 콘솔 출력 함수
        // 컴포넌트가 마운트될 때 한 번 실행
        handleResize();

        // 이벤트 리스너 등록
        window.addEventListener("resize", handleResize);

        // 컴포넌트가 언마운트될 때 이벤트 리스너 해제
        return () => {
            window.removeEventListener("resize", handleResize);
        };
    }, []);

    useEffect(() => {
        if (editCommentId && textareaRef.current) {
            autoResizeTextarea();
        }
    }, [editContent, editCommentId]);
    
    const fetchPostDetail = (postUuid: string) => {
        getPostDetail(postUuid)
        .then((response) => {
            console.log(response);
            setPostDetailData(response.post);
            setComments(response.comments);
            setLikeCount(response.post.likeCount);
        })
        .catch((error) => {
            console.error("데이터가져오는데 실패", error);
        })
    }

    const fetchLikeList = () => {
        console.log(" 좋아요 여기까지옴?")
        console.log(`userinfo 상태는? ${userInfo}`)
        if(userInfo){
            getUserLikedPosts(userInfo.sub)
            .then((response) => {
                const isLiked = response.some((likedPost: any) => likedPost.postUuid === postUuid);

                console.log(`${postUuid}가 좋아요 목록에 있는지 확인 중...`);
                
                if (isLiked) {
                    setIsLike(true);
                } else {
                    setIsLike(false);
                }
            })
            .catch((error) => {
                console.error("데이터가져오는데 실패", error);
            })
        }
    }
    const handleScroll = () => {
        const scrollY = window.scrollY;

        // 스크롤이 200px을 넘으면 버튼을 위쪽에 고정
        if (scrollY > 200) {
            setLikeBtnPosition("20%");
        } else {
            setLikeBtnPosition("50%");
        }
    };
    const handleResize = () => {
        if (window.innerWidth <= 1080) {
            console.log("현재 브라우저 너비는 1080px 이하입니다.");
            setIsSideVisible(false);
        } else {
            console.log("현재 브라우저 너비는 1080px 초과입니다.");
            setIsSideVisible(true);
            setIsMobileMenuOpen(false); // 화면 크기가 커지면 모바일 메뉴 닫기
        }
    };

    const autoResizeTextarea = () => {
        if (textareaRef.current) {
            textareaRef.current.style.height = 'auto'; // 높이 초기화
            textareaRef.current.style.height = `${textareaRef.current.scrollHeight}px`; // 내용에 맞게 높이 조정
        }
    };

    const handleTitleChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
        setContent(e.target.value);
    }

    const handleCommentSumit = async () => {
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
            setContent(''); // 입력 필드 초기화
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

    const handleEditClick = (comment: CommentData) => {
        setEditCommentId(comment.commentUuid);
        setEditUserId(comment.userUuid);
        setEditContent(comment.content);
    };

    const handleEditSubmit = async () => {
        if (!editCommentId) return;
    
        const token = keycloak?.token;
        const nickname = userInfo?.nickname;

        if (!token || !nickname) {
            alert("로그인이용자만 사용가능합니다")
            return;
        }
        if (userInfo?.sub !== editUserId){
            alert("자신의 댓글이 아닙니다");
            return;
        }
        
        updateComment(
            {
                content: editContent,
            },
            editCommentId,
            token
        ).then((response) => {
            console.log("Comment updated successfully:", response);
            // todo: 여기서 최종적 일관성
            // 댓글 리스트에서 해당 댓글을 업데이트
            setComments((prevComments) =>
                prevComments.map((comment) =>
                    comment.commentUuid === editCommentId
                        ? {
                            ...comment,
                            content: response.content,
                            updatedDate: response.updatedDate,
                        }
                        : comment
                )
            );
            setEditCommentId(null);
            setEditUserId(null);
            setEditContent("");
        })
        .catch((error) => {
            console.error("Error updating comment:", error);
            alert("댓글 수정에 실패하였습니다");
        })
        
    };

    const handleEditCancel = () => {
        // 수정 모드 종료
        setEditCommentId(null);
        setEditUserId(null);
        setEditContent("");
    }

    const handleDeleteComment = async (userUuid: string, commentUuid: string) => {
        console.log(`댓글 삭제 요청 ${commentUuid}`)
        const token = keycloak?.token;
        const nickname = userInfo?.nickname;

        if (!token || !nickname) {
            alert("로그인이용자만 사용가능합니다")
            return;
        }

        if (userInfo?.sub !== userUuid){
            alert("자신의 댓글이 아닙니다");
            return;
        }

        deleteComment(
            commentUuid,
            token
        )
        .then(() => {
            alert("댓글삭제 성공");
            // 댓글 목록에서 삭제된 댓글을 제외하고 새로운 목록 설정
            setComments((prevComments) =>
                prevComments.filter((comment) => comment.commentUuid !== commentUuid)
            );
        })
        .catch((error) => {
            console.error("Error deleting comment:", error);
        })
    }

    const handleDeletePost = async (userUuid: string, postUuid: string) => {
        console.log(`게시글 삭제 요청 ${postUuid}`)
        const token = keycloak?.token;
        const nickname = userInfo?.nickname;

        if (!token || !nickname) {
            alert("로그인이용자만 사용가능합니다")
            return;
        }

        if (userInfo?.sub !== userUuid){
            alert("자신의 게시글이 아닙니다");
            return;
        }

        deletePost(
            postUuid,
            token
        )
        .then(() => {
            alert("댓글삭제 성공");
            toHome();
        })
        .catch((error) => {
            console.error("Error deleting post:", error);
        })
    }

    const handleIncrementViewcount = async (postUuid: string) => {
        incrementViewcount(
            postUuid
        )
        .then(() => {
            console.log("조회주 증가");
        })
        .catch((error) => {
            console.error("Error Increment viewcount:", error);
        })
    }

    const handleLikeToggle = async () => {
        const token = keycloak?.token;
        const nickname = userInfo?.nickname;

        if (!token || !nickname) {
            alert("로그인이용자만 사용가능합니다")
            return;
        }

        toggleLike(
            postDetailData.postUuid,
            token,
        )   
        .then(() => {
            // 좋아요 상태 토글
            setIsLike((prevIsLike) => {
                const newIsLike = !prevIsLike;
                // likeCount 업데이트
                setLikeCount((prevCount) => newIsLike ? prevCount + 1 : prevCount - 1);
                return newIsLike;
            });
        })
        .catch((error) => {
            console.error("Error creating comment:", error);
        })
    }

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
            <div className="flex justify-center min-h-screen bg-gray-50">
                <div className="flex w-full max-w-screen-lg">
                    {/* 왼쪽 목록 영역 */}
                    {(isSideVisible || isMobileMenuOpen) && (
                    <div
                        className={`w-80 p-4 bg-gray-100 h-screen overflow-y-auto 
                        ${isMobileMenuOpen && 'fixed left-0 top-0 translate-x-0 shadow-custom-default'}`
                    }>

                        <div className="flex justify-between mb-4">
                            <div className="text-xl font-bold ">게시글 목록</div>
                            {isMobileMenuOpen && (
                                <button className="w-8 h-8 flex items-center justify-center border border-gray-300 rounded-full hover:bg-gray-200 ">
                                    <BsX size={35} onClick={() => setIsMobileMenuOpen(false)}/>
                                </button>
                            )}
                            
                        </div>
                        <ul className="space-y-2">
                            <li className="p-2 bg-white shadow rounded cursor-pointer hover:bg-gray-200">
                                게시글 타이틀 1
                            </li>
                            <li className="p-2 bg-white shadow rounded cursor-pointer hover:bg-gray-200">
                                게시글 타이틀 2
                            </li>
                            <li className="p-2 bg-white shadow rounded cursor-pointer hover:bg-gray-200">
                                게시글 타이틀 3
                            </li>
                            <li className="p-2 bg-white shadow rounded cursor-pointer hover:bg-gray-200">
                                게시글 타이틀 4
                            </li>
                            <li className="p-2 bg-white shadow rounded cursor-pointer hover:bg-gray-200">
                                게시글 타이틀 5
                            </li>
                        </ul>
                    </div>
                    )}

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
                                    <div 
                                        className="cursor-pointer hover:text-black"
                                        onClick={() => toPostRewrite(postDetailData.postUuid)}
                                    >
                                        수정
                                    </div>
                                    <div 
                                        className="cursor-pointer hover:text-black"
                                        onClick={() => handleDeletePost(postDetailData.userUuid, postDetailData.postUuid)}
                                    >
                                        삭제
                                    </div>
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
                                                {editCommentId !== comment.commentUuid && (
                                                    <div 
                                                        className="cursor-pointer hover:text-black"
                                                        onClick={() => handleEditClick(comment)}
                                                    >
                                                        수정
                                                    </div>    
                                                )}
                                                
                                                <div 
                                                    className="cursor-pointer hover:text-black"
                                                    onClick={() => handleDeleteComment(comment.userUuid, comment.commentUuid)}
                                                >
                                                    삭제
                                                </div>
                                            </div>
                                        )}
                                    </div>
                                    {editCommentId === comment.commentUuid ? (
                                        // 수정 모드
                                        <div className="mt-4">
                                            <textarea
                                                ref={textareaRef}
                                                className="p-3 w-full focus:outline-none border border-gray-300 rounded-md resize-none"
                                                value={editContent}
                                                onChange={(e) => setEditContent(e.target.value)}
                                                style={{ minHeight: "100px", overflow: "hidden" }}
                                                onInput={(e) => {
                                                    const target = e.target as HTMLTextAreaElement;
                                                    target.style.height = "auto"; // 높이 초기화
                                                    target.style.height = `${target.scrollHeight}px`; // 높이를 내용에 맞게 조정
                                                }}
                                            />
                                            <div className="mt-4 flex justify-end">
                                                <div className="flex gap-2">
                                                    <button
                                                        className="bg-blue-500 text-white px-6 py-2 rounded cursor-pointer hover:bg-blue-600"
                                                        onClick={handleEditCancel}
                                                    >
                                                        취소
                                                    </button>
                                                    <button
                                                        className="bg-blue-500 text-white px-6 py-2 rounded cursor-pointer hover:bg-blue-600"
                                                        onClick={handleEditSubmit}
                                                    >
                                                        댓글 수정
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    ) : (
                                        // 기본 모드
                                        <div 
                                            className="mt-4 leading-loose"
                                            dangerouslySetInnerHTML={{ __html: DOMPurify.sanitize(comment.content.replace(/\n/g, '<br>')) }}
                                        >
                                        </div>
                                    )}
                                </div>
                            ))
                        ) : (
                            <div className="py-6 text-center text-gray-500">댓글이 없습니다.</div>
                        )}
                        </div>
                    </div>
                </div>
            </div>

            {/* ♥ 버튼 */}
            {isSideVisible && (
                <button
                    className="fixed right-8 p-4 rounded-full shadow-lg bg-gray-100"
                    style={{ 
                        top: likeBtnPosition, 
                        right: "calc(50% - 610px)", // 본문 너비에 맞춘 오른쪽 여백 설정
                        transition: "top 0.3s ease" }}
                >
                    {isLike ? (
                        // 좋아요를 누른 상태 (빨간색 배경에 비어있는 하트)
                        <div
                            className="w-10 h-10 flex items-center justify-center rounded-full bg-red-500 hover:bg-red-600 text-white"
                            onClick={handleLikeToggle}
                        >
                            <FaHeart size={17}/>
                        </div>
                    ) : (
                        // 좋아요를 누르지 않은 상태 (검은색 테두리에 검은색 하트)
                        <div
                            className="w-10 h-10 flex items-center justify-center rounded-full border-2 border-black text-black hover:bg-gray-200"
                            onClick={handleLikeToggle}
                        >
                            <FaHeart size={17}/>
                        </div>
                    )}
                    <div>{likeCount}</div>
                </button>
            )}

            {/* 헤더 아래에 ♥ 버튼 */}
            {!isSideVisible && (
                <div className="fixed top-20 right-5">
                    <button
                        className="p-4 rounded-full shadow-lg bg-gray-100"
                        onClick={handleLikeToggle}
                    >
                        {isLike ? (
                            <div className="w-10 h-10 flex items-center justify-center rounded-full bg-red-500 hover:bg-red-600 text-white">
                                <FaHeart size={17} />
                            </div>
                        ) : (
                            <div className="w-10 h-10 flex items-center justify-center rounded-full border-2 border-black text-black hover:bg-gray-200">
                                <FaHeart size={17} />
                            </div>
                        )}
                    </button>
                </div>
            )}
            {!isSideVisible && !isMobileMenuOpen && (
                <div className="fixed top-20 left-5">
                    <button
                        className="p-4 rounded-full shadow-lg bg-gray-100"
                        onClick={() => setIsMobileMenuOpen(true)}
                    >
                        <div className="w-10 h-10 flex items-center justify-center rounded-full   text-black hover:bg-gray-200">
                            <BsList size={40} />
                        </div>
                    </button>
                </div>
            )}
            
        </div>
    );
}

export default PostDetailPage;



