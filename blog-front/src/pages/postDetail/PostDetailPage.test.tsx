import React, { useEffect, useRef, useState } from "react"; 
import Keycloak from "keycloak-js";
import SkeletonCommonHeader from "../../components/skeleton/SkeletonCommonHeader";
import SkeletonPostDetailBody from "../../components/skeleton/SkeletonPostDetailBody";
import Header from "../../components/header/common/Header";
import { BsSearch } from "react-icons/bs";
import { FaAlignJustify, FaAngleLeft, FaHeart } from "react-icons/fa";
import { TiArrowDownThick, TiArrowUpThick } from "react-icons/ti";
import { useLocation, useParams } from "react-router-dom";
import { formatDate } from "../../util/dateUtil";
import useNavigationHelper from "../../util/navigationUtil";
import { deletePost } from "../../service/postService";
import { getPostDetail } from "../../service/queryService.public";
import { incrementViewcount } from "../../service/viewcountService";
import { getNicknamePosts, getNicknameProfile, getUserLikedPosts } from "../../service/queryService.auth";
import DOMPurify from "dompurify";
import { createComment, deleteComment, updateComment } from "../../service/commentService";
import { toggleLike } from "../../service/likeService";
import ApiFailModal from "../../components/modal/ApiFailModal";
import AuthPostModal from "../../components/modal/AuthPostModal";
import AuthCommentModal from "../../components/modal/AuthCommentModal";
import AuthModal from "../../components/modal/AuthModal";
import AuthRefreshModal from "../../components/modal/AuthRefreshModal";

interface Props {
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

interface NicknamePostsData{
    postUuid: string;
    userUuid: string;
    nickname: string;
    title: string;
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

const TestPostDetailPage: React.FC<Props> = ({ keycloak, keycloakStatus }) => {
    const {toPostRewrite, toUserBlogSearch, toUserBlog, toPostDetail } = useNavigationHelper();
    const { nickname, postUuid} = useParams();
    const location = useLocation();
    const [kecloakLoading, isKecloakLoading] = useState<boolean>(true);
    const [isUserInfoLoaded, setIsUserInfoLoaded] = useState<boolean>(false);
    const [userInfo, setUserInfo] = useState<null | Record<string, any>>(null);

    const [isModalLoading, setIsModalLoading] = useState(false);
    const [hasError, setHasError] = useState<boolean>(false);

    const [postDetailData, setPostDetailData] = useState<PostDetailData>(initialPostDetailData);
    const [newComment, setNewComment] = useState<string>('');
    const [comments, setComments] = useState<CommentData[]>([]);
    const [isLike, setIsLike] = useState<boolean>(false);
    const [likeCount, setLikeCount] = useState<number>(0);
    const [blogPosts, setBlogPosts] = useState<NicknamePostsData[]>([]);
    const [blogTitle, setBlogTitle] = useState<string>('');
    const [blogIntro, setBlogIntro] = useState<string>('');

    const [editCommentId, setEditCommentId] = useState<string | null>(null);
    const [editUserId, setEditUserId] = useState<string | null>(null);
    const [editContent, setEditContent] = useState<string>("");

    const textareaRef = useRef<HTMLTextAreaElement>(null);

    const [keyword, setKeyword] = useState("");

    const [showRemote, setShowRemote] = useState<boolean>(true);
    const [showSlide, setShowSlide] = useState<boolean>(false);

    const [isModalOpen, setIsModalOpen] = useState(false);
    const [isApiFailModalOpen, setIsApiFailModalOpen] = useState(false);
    const [isAuthPostModalOpen, setIsAuthPostModalOpen] = useState(false);
    const [isAuthCommentModalOpen, setIsAuthCommentModalOpen] = useState(false);
    const [isAuthModalOpen, setIsAuthModalOpen] = useState(false);
    const [isAuthRefreshModalOpen, setIsAuthRefreshModalOpen] = useState(false);

    // 1번
    useEffect(() => {
        if (keycloakStatus !== "loading") {
            if(keycloakStatus === "unauthenticated" || keycloakStatus === "authenticated") {
                console.log(`키클락 상태 : ${keycloakStatus}`)
                isKecloakLoading(false);
                setHasError(false);
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
    }, [keycloak, location.state])

    // 2번
    useEffect(() => {
        if(kecloakLoading) return;

        if(!(nickname && postUuid)) return;
        // location.state에서 사전 읽기 데이터를 확인
        if (location.state?.postDetailData) {
            setPostDetailData(location.state.postDetailData);
            fetchComments(postUuid);
            setBlogPosts([location.state.postDetailData]);
            // 추가 데이터 API 호출
            getNicknamePosts(nickname)
            .then((response) => {
                console.log(response);

                setBlogPosts((prevPosts) => {
                    if (prevPosts.length > 0) {
                        // 현재 데이터가 있는 경우
                        const existingIndex = response.findIndex(
                            (post: NicknamePostsData) => post.postUuid === location.state.postDetailData.postUuid
                        );

                        if (existingIndex !== -1) {
                            // response에 해당 postUuid가 있다면 수정
                            const updatedResponse = [...response];
                            updatedResponse[existingIndex] = {
                                ...updatedResponse[existingIndex],
                                ...location.state.postDetailData,
                            };
                            return updatedResponse;
                        } else {
                            // response에 해당 postUuid가 없다면 배열의 맨 앞에 추가
                            return [location.state.postDetailData, ...response];
                        }
                    } else {
                        // 현재 데이터가 없는 경우 기존 로직 유지
                        return response;
                    }
                });
            })
            .catch((error) => {
                console.error("데이터 가져오는데 실패", error);
            });
        } else {
            // 사전 읽기 데이터가 없으면 서버에서 데이터 요청
            fetchPostDetail(postUuid);
            fetchIncrementViewcount(postUuid);
            fetchNicknamePosts(nickname);
        }
        setShowRemote(true);
        fetchNicknameProfile(nickname);

        console.log(postDetailData);

    }, [postUuid, kecloakLoading]);

    useEffect(() => {
        console.log(`isUserInfoLoaded 상태 : ${isUserInfoLoaded}`);
        if (isUserInfoLoaded) {
            setIsLike(false);
            fetchLikeList();
        }
    }, [postUuid,isUserInfoLoaded]);

    useEffect(() => {
        if (editCommentId && textareaRef.current) {
            autoResizeTextarea();
        }
    }, [editContent, editCommentId]);

    // 화면의 다른 곳을 클릭하면 리모콘을 토글
    useEffect(() => {
        const handleClickOutside = (e: MouseEvent) => {
        // 버튼 영역을 제외하고 클릭했을 때만 실행
        if (!(e.target as HTMLElement).closest(".remote-button")) {
            setShowRemote((prev) => !prev);
        }
        };

        document.addEventListener("click", handleClickOutside);

        return () => {
        document.removeEventListener("click", handleClickOutside);
        };
    }, []);

    // 화면 크기가 1440px 이하일 때 showSlide를 false로 설정
    useEffect(() => {
        const handleResize = () => {
            if (window.innerWidth <= 1440) {
                setShowSlide(false);
            }
        };

        // 초기 실행
        handleResize();

        window.addEventListener("resize", handleResize);

        return () => {
            window.removeEventListener("resize", handleResize);
        };
    }, []);

    const fetchPostDetail = (postUuid: string) => {
        getPostDetail(postUuid)
        .then((response) => {
            console.log(response);
            setPostDetailData(response.post);
            setComments(response.comments);
            setLikeCount(response.post.likeCount);
            setHasError(false);
        })
        .catch(() => {
            setHasError(true);
        })
    }

    const fetchComments = (postUuid: string) => {
        getPostDetail(postUuid)
        .then((response) => {
            console.log(response.comments);
            setComments(response.comments);
            setLikeCount(response.post.likeCount);
            setHasError(false);
        })
        .catch(() => {
            setHasError(false); //todo: 
        })
    }

    const fetchLikeList = () => {
        console.log(" 좋아요 여기까지옴?")
        console.log(`userinfo 상태는? ${userInfo}`)
        if(userInfo){
            getUserLikedPosts(userInfo.sub)
            .then((response) => {
                const isLiked = response.content.some((likedPost: any) => likedPost.postUuid === postUuid);

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

    const fetchIncrementViewcount = async (postUuid: string) => {
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

    const fetchNicknameProfile = (nickname: string) => {
        getNicknameProfile(nickname)
        .then((response) => {
            console.log(response);
            setBlogTitle(response.title);
            setBlogIntro(response.intro);
        })
        .catch((error) => {
            console.error("데이터 가져오는데 실패", error);
            setBlogTitle(`${nickname}'s blog`);
            setBlogIntro("");
        })
    }

    const handleDeletePost = async (userUuid: string, postUuid: string) => {
        console.log(`게시글 삭제 요청 ${postUuid}`)
        const token = keycloak?.token;
        const nickname = userInfo?.nickname;

        if (!token || !nickname) {
            setIsAuthRefreshModalOpen(true);
            return;
        }

        if (userInfo?.sub !== userUuid){
            setIsAuthPostModalOpen(true);
            return;
        }

        deletePost(
            postUuid,
            token
        )
        .then(() => {
            handleConfirmDelete();
        })
        .catch(() => {
            setIsApiFailModalOpen(true);
        })
    }

    const fetchNicknamePosts = (nickname: string) => {
        getNicknamePosts(nickname)
        .then((response) => {
            console.log(response);
            setBlogPosts(response);
        })
        .catch((error) => {
            console.error("데이터가져오는데 실패", error);
        })
    }

    const handleNewComentChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
        setNewComment(e.target.value);
    }

    const handleNewCommentSumit = async () => {
        const token = keycloak?.token;
        const nickname = userInfo?.nickname;

        if (!token || !nickname) {
            setIsAuthRefreshModalOpen(true);
            return;
        }

        createComment(
            {
                postUuid: postDetailData.postUuid,
                nickname: nickname,
                content: newComment,
            },
            token
        )
        .then((response) => {
            console.log("Comment created successfully:", response);
            setNewComment(''); // 입력 필드 초기화
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
        .catch(() => {
            setIsApiFailModalOpen(true);
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
            setIsAuthRefreshModalOpen(true);
            return;
        }
        if (userInfo?.sub !== editUserId){
            setIsAuthCommentModalOpen(true);
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
        .catch(() => {
            setIsApiFailModalOpen(true);
        })
    };

    const handleEditCancel = () => {
        // 수정 모드 종료
        setEditCommentId(null);
        setEditUserId(null);
        setEditContent("");
    }

    const handleLikeToggle = async () => {
        const token = keycloak?.token;
        const nickname = userInfo?.nickname;

        if (!token || !nickname) {
            setIsAuthModalOpen(true);
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
        .catch(() => {
            setIsApiFailModalOpen(true);
        })
    }

    const toggleSlide = () => {
        setShowSlide((prev) => !prev);
    };

    const handleDeleteComment = async (userUuid: string, commentUuid: string) => {
        console.log(`댓글 삭제 요청 ${commentUuid}`)
        const token = keycloak?.token;
        const nickname = userInfo?.nickname;

        if (!token || !nickname) {
            setIsAuthRefreshModalOpen(true);
            return;
        }

        if (userInfo?.sub !== userUuid){
            setIsAuthCommentModalOpen(true);
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
    const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
        if (e.key === "Enter") {
            toUserBlogSearch(nickname!, keyword);
        }
    };

      // 모달 열기
    const handleModalOpen = () => {
        setIsModalOpen(true);
    };

    // 모달 닫기
    const handleModalClose = () => {
        setIsModalOpen(false);
    };

    // 삭제 확정(예시)
    const handleConfirmDelete = async  () => {
        setIsModalLoading(true); // 로딩 시작

        // 3초 지연 (또는 실제 API 호출)
        setTimeout(() => {
          // 작업 완료 후 로딩 해제 + 모달 닫기
          setIsModalLoading(false);
          setIsModalOpen(false);
          toUserBlog(userInfo?.nickname);
        }, 3000);
    };

    const autoResizeTextarea = () => {
        if (textareaRef.current) {
            textareaRef.current.style.height = 'auto'; // 높이 초기화
            textareaRef.current.style.height = `${textareaRef.current.scrollHeight}px`; // 내용에 맞게 높이 조정
        }
    };

    const handleCloseApiFailModal = () => {
        setIsApiFailModalOpen(false);
    }
    const handleCloseAuthModal = () => {
        setIsAuthModalOpen(false);
      }

    if (!keycloak) {
        return (
            <>
                <SkeletonCommonHeader/>
                <SkeletonPostDetailBody/>
            </>
        )
    }

    if (hasError) {
        return (
            <>
                <Header keycloak={keycloak} keycloakStatus={keycloakStatus}/>
                <div className="min-w-full min-h-screen flex justify-center  bg-gray-100">
                    <div className="max-w-1728 2xl:max-w-1376 xl:max-w-1024  flex-1 flex justify-center bg-white shadow-lg">
                        <div className="relative pt-32 pb-16 w-[768px] xs:w-full xs:px-4  2xs:px-2 flex flex-col">
                            {/* 왼쪽 게시글 목록 */}
                            <aside className="absolute top-0 -left-72 w-64 min-h-72 flex h-full xl:hidden bg-white">
                                {/* Inner Box */}
                                <div className="mt-40 mb-10 py-4 w-full max-h-full flex flex-col rounded-md">
                                    <div // 블로그 이름
                                        className="px-2 py-4 text-xl bg-gray-100 rounded-md mb-2 text-center font-bold cursor-pointer"
                                        onClick={() => toUserBlog(nickname!)}>
                                        {blogTitle}
                                    </div>
                                    <div 
                                        className="px-2 py-4 rounded-md border border-gray-300 text-sm bg-white  empty:hidden break-words whitespace-pre-line">
                                        {blogIntro}
                                    </div>
                                    <div className="p-2 pt-4">
                                        <div className="px-2 py-1 border border-gray-400 flex  justify-center items-center">
                                            <BsSearch size={20}/>
                                            <input 
                                                className="pl-2 w-full outline-none"
                                                type="text"                                                 
                                                placeholder="검색어를 입력하세요"
                                                value={keyword}
                                                onChange={(e) => setKeyword(e.target.value)}
                                                onKeyDown={handleKeyDown}/>
                                        </div>
                                    </div>
                                    <div className="py-4 px-2 text-base font-bold">
                                        게시글 목록
                                    </div>
                                    <div // 글목록
                                        className="py-4 px-2 overflow-y-auto ">
                                        <ul className="space-y-4 border-l-2 border-gray-300">
                                            {blogPosts.map((post) => (
                                                <li 
                                                    key={post.postUuid}
                                                    className=" hover-text-blink px-2 text-sm truncate cursor-pointer"
                                                    onClick={() => toPostDetail(post.nickname, post.postUuid)}>
                                                    {post.title}
                                                </li>
                                            ))}
                                        </ul>
                                    </div>
                                </div>
                            </aside>
                            {/* 빈 리스트 */}
                            {hasError && (
                                <div className="flex flex-col justify-center items-center">
                                    <div className="w-[425px] 2xs:w-[325px] ">
                                        <img
                                            className="ml-4" 
                                            src="https://images.ghtjr.com/e94915c9-e28d-4217-ad2e-e30a7a685d7c_empty_box_miniblog.png" alt="" />
                                    </div>
                                    <div className="mt-6 text-2xl 2xs:text-xl text-gray-500">
                                        게시글을 찾을 수 없습니다.
                                    </div>
                                </div>
                            )}
                        </div>
                    </div>
                </div>
            </>
        )
    }

    return (
        <>
            <Header keycloak={keycloak} keycloakStatus={keycloakStatus}/>
            {/* 전체 바디 (빈공간 포함) */}
            <div className="min-w-full min-h-screen flex justify-center  bg-gray-100">
                {/* 본문바디 (빈공간 미포함) */}
                <div className="max-w-1728 2xl:max-w-1376 xl:max-w-1024 pt-20 pb-4 px-4 flex-1 flex justify-center bg-white shadow-lg">
                    {/* 게시글 섹션 */}
                    <div className="relative pt-32 pb-16 w-[768px] xs:w-full xs:px-4  2xs:px-2 flex flex-col">
                        {/* 왼쪽 게시글 목록 */}
                        <aside className="absolute top-0 -left-72 w-64 min-h-72 flex h-full xl:hidden bg-white">
                            {/* Inner Box */}
                            <div className="mt-40 mb-10 py-4 w-full max-h-full flex flex-col rounded-md">
                                <div // 블로그 이름
                                    className="px-2 py-4 text-xl bg-gray-100 rounded-md mb-2 text-center font-bold cursor-pointer"
                                    onClick={() => toUserBlog(nickname!)}>
                                    {blogTitle}
                                </div>
                                <div 
                                    className="px-2 py-4 rounded-md border border-gray-300 text-sm bg-white  empty:hidden break-words whitespace-pre-line">
                                    {blogIntro}
                                </div>
                                <div className="p-2 pt-4">
                                    <div className="px-2 py-1 border border-gray-400 flex  justify-center items-center">
                                        <BsSearch size={20}/>
                                        <input 
                                            className="pl-2 w-full outline-none"
                                            type="text"                                                 
                                            placeholder="검색어를 입력하세요"
                                            value={keyword}
                                            onChange={(e) => setKeyword(e.target.value)}
                                            onKeyDown={handleKeyDown}/>
                                    </div>
                                </div>
                                
                                <div className="py-4 px-2 text-base font-bold">
                                    게시글 목록
                                </div>
                                <div // 글목록
                                    className="py-4 px-2 overflow-y-auto ">
                                    <ul className="space-y-4 border-l-2 border-gray-300">
                                        {blogPosts.map((post) => (
                                            <li 
                                                key={post.postUuid}
                                                className=" hover-text-blink px-2 text-sm truncate cursor-pointer"
                                                onClick={() => toPostDetail(post.nickname, post.postUuid)}>
                                                {post.title}
                                            </li>
                                        ))}
                                    </ul>
                                </div>
                            </div>
                        </aside>
                        <div className={``}>
                            <div className="text-5xl font-bold bg-gray-50 rounded-lg leading-snug">
                                {postDetailData.title}
                            </div>
                        </div>
                        {/* 게시글 정보 */}
                        <div className={`flex mt-8 pb-8 justify-between border-b border-gray-100`}>
                            <div className="flex text-base gap-2">
                                <div className="font-bold cursor-pointer" onClick={() => toUserBlog(postDetailData.nickname)}>
                                    {postDetailData.nickname}
                                </div>
                                <div>　</div>
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
                                        onClick={handleModalOpen}
                                        
                                    >
                                        삭제
                                    </div>
                                </div>
                            )}
                        </div>
                        {/* 내용 */}
                        <div className="mt-4" dangerouslySetInnerHTML={{ __html: DOMPurify.sanitize(postDetailData.content) }}>
                        </div>
                        {/* 댓글 작성 */}
                        <div className={` bg-gray-100 flex-col border-t`}>
                            <div className="mt-2 font-bold text-lg">
                                {postDetailData.commentCount}개의 댓글
                            </div>
                            <textarea
                                className="p-3 mt-2 w-full focus:outline-none border border-gray-300 rounded-md resize-none"
                                value={newComment}
                                onChange={handleNewComentChange}
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
                                    className="bg-blue-500 text-white px-6 py-1.5 font-black text-lg rounded cursor-pointer hover:bg-blue-600"
                                    onClick={handleNewCommentSumit}>
                                    댓글 작성
                                </button>
                            </div>
                        </div>
                        {/* 댓글 리스트 */}
                        <div className={`mt-4 bg-gray-100 flex flex-col`}>
                            {comments.length > 0 ? (
                                comments.map((comment) => (
                                    <div key={comment.commentUuid} className="py-6  flex flex-col border-t border-gray-300">
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
                                                            className="px-6 py-1.5 text-lg rounded bg-green-400  font-bold text-white hover:bg-green-500 cursor-pointer"
                                                            onClick={handleEditSubmit}>
                                                            수정
                                                        </button>
                                                        <button
                                                            className="ml-2 px-6 py-1.5 text-lg rounded bg-red-400 font-bold text-white hover:bg-red-600 cursor-pointer"
                                                            onClick={handleEditCancel}>
                                                            취소
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

            {/* 리모콘 */}
            {showRemote && (
                <aside className="fixed flex flex-col gap-4 top-[calc(50%-96px)] right-[calc(50%-468px)] sm:right-2">
                    <button // 게시글 목록 슬라이드
                        className=" w-10 h-10 bg-gray-700 hover:bg-gray-500 text-white rounded-md hidden xl:flex items-center justify-center"
                        onClick={toggleSlide}
                    >
                        <FaAlignJustify size={25} />
                    </button>
                    <button // 게시글 목록 슬라이드
                        className={`remote-button w-10 h-10 bg-gray-700 hover:bg-gray-500 rounded-md flex items-center justify-center ${isLike ? "text-red-500" : "text-white"}`}
                        onClick={handleLikeToggle}
                    >
                        <FaHeart size={25} />
                    </button>
                    <button // 위로 이동
                        className="remote-button w-10 h-10 bg-gray-700 hover:bg-gray-500 text-white rounded-md flex items-center justify-center"
                        onClick={() => window.scrollTo({ top: 0, behavior: "smooth" })}
                    >
                        <TiArrowUpThick size={25} />
                    </button>
                    <button // 아래로 이동
                        className="remote-button w-10 h-10 bg-gray-700 hover:bg-gray-500 text-white rounded-md flex items-center justify-center"
                        onClick={() => window.scrollTo({ top: document.body.scrollHeight, behavior: "smooth" })}
                    >
                        <TiArrowDownThick size={25} />
                    </button>
                </aside>
            )}

            {/* 슬라이드 메뉴 */}
            <div
                className={`remote-button hidden xl:flex flex-col fixed top-0 left-0 w-64 h-screen bg-gray-50 shadow-md text-black z-20 transform ${
                showSlide ? " translate-x-0" : "-translate-x-full"
                } transition-transform duration-300`}
            >
                <div className="h-16 p-4  flex justify-between items-center bg-white">
                    <div 
                        className="text-lg text-center font-bold cursor-pointer"
                        onClick={() => toUserBlog(nickname!)}>
                        {blogTitle}
                    </div>
                    <button 
                        className="text-gray-400 hover:text-black"
                        onClick={toggleSlide}>
                        <FaAngleLeft size={20} />
                    </button>
                </div>
                <div className="p-2 bg-white border-b border-gray-200">
                    <div className="px-2 py-1 border border-gray-400 flex  justify-center items-center">
                        <BsSearch size={20}/>
                        <input 
                            className="remote-button pl-2 w-full outline-none"
                            type="text"                                                 
                            placeholder="검색어를 입력하세요"
                            value={keyword}
                            onChange={(e) => setKeyword(e.target.value)}
                            onKeyDown={handleKeyDown}/>
                    </div>
                </div>
                <div className="px-4 pt-4 text-base font-bold">
                    게시글 목록
                </div>
                <ul className="px-4 py-8 space-y-4  overflow-y-auto">
                    {blogPosts.map((item) => (
                        <li 
                            key={item.postUuid}
                            className="remote-button hover-text-blink px-2 text-sm truncate cursor-pointer"
                            onClick={() => toPostDetail(item.nickname, item.postUuid)}>
                            {item.title}
                        </li>
                    ))}
                </ul>
            </div>

            {/* 슬라이드 오버레이 */}
            {showSlide && (
                <div
                    className="fixed top-0 hidden xl:flex left-0 w-full h-full bg-black opacity-50 z-10"
                    onClick={toggleSlide}>
                </div>
            )}
            {isModalOpen && (
                <>
                    <div className="remote-button fixed inset-0  bg-black opacity-25  z-30">
                    </div>
                    <div className="remote-button fixed inset-0 flex items-center justify-center bg-transparent  z-50">
                        {/* 모달 컨테이너 */}
                        <div className="bg-gray-100 rounded-md shadow-md w-full max-w-sm mx-4 p-6 ">
                            {/* 제목 */}
                            <h3 className="text-xl font-bold mb-4">게시글 삭제</h3>
                            {/* 설명 문구 */}
                            <p className="mb-6 text-gray-700">정말로 삭제하시겠습니까?</p>

                            {/* 버튼 영역 */}
                            <div className="flex justify-end gap-2">
                            <button
                                className="px-4 py-2 text-sm font-medium text-gray-600 bg-gray-200 rounded hover:bg-gray-300 transition"
                                onClick={handleModalClose}
                            >
                                취소
                            </button>
                            <button
                                className={`px-4 py-2 text-sm font-medium rounded transition 
                                    ${isModalLoading ? 'bg-green-300 cursor-not-allowed' : 'bg-green-500 hover:bg-green-600 text-white'}
                                `}
                                onClick={() => handleDeletePost(postDetailData.userUuid, postDetailData.postUuid)}
                                disabled={isModalLoading} // 로딩 중에는 클릭 불가능
                            >
                                {isModalLoading ? (
                                    // 로딩 중일 때 표시할 영역
                                    <div className="flex items-center gap-2">
                                    <div className="w-4 h-4 border-2 border-t-transparent border-white rounded-full animate-spin" />
                                    <span>처리 중...</span>
                                    </div>
                                ) : (
                                    // 평소 표시(확인 버튼)
                                    <span>확인</span>
                                )}
                            </button>
                            </div>
                        </div>
                    </div>
                </>
                
            )}
            {isApiFailModalOpen &&(
                <ApiFailModal onClose={handleCloseApiFailModal}/>
            )}
            {isAuthPostModalOpen && (
                <AuthPostModal/>
            )}
            {isAuthCommentModalOpen && (
                <AuthCommentModal/>
            )}
            {isAuthModalOpen && (
                <AuthModal onClose={handleCloseAuthModal}/>
            )}
            {isAuthRefreshModalOpen && (
                <AuthRefreshModal/>
            )}

        </>
    )
}

export default TestPostDetailPage;

// const [isApiFailModalOpen, setIsApiFailModalOpen] = useState(false);
// const [isAuthPostModalOpen, setIsAuthPostModalOpen] = useState(false);
// const [isAuthCommentModalOpen, setIsAuthCommentModalOpen] = useState(false);
// const [isAuthModalOpen, setIsAuthModalOpen] = useState(false);
// const [isAuthRefreshModalOpen, setIsAuthRefreshModalOpen] = useState(false);