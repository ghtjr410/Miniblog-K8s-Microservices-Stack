import React, { useEffect, useRef, useState } from "react";
import Keycloak from "keycloak-js";
import Header from "../../components/header/common/Header";
import { useParams, useSearchParams } from "react-router-dom";
import { BsSearch } from "react-icons/bs";
import { FaAlignJustify, FaAngleLeft, FaHeart, FaRegComment } from "react-icons/fa";
import { PiEyeLight } from "react-icons/pi";
import { TiArrowDownThick, TiArrowUpThick } from "react-icons/ti";
import SkeletonCommonHeader from "../../components/skeleton/SkeletonCommonHeader";
import SkeletonBlogSearchBody from "../../components/skeleton/SkeletonBlogSearchBody";
import { getNicknamePosts, getNicknameProfile, searchNicknamePostsByText } from "../../service/queryService.auth";
import useNavigationHelper from "../../util/navigationUtil";
import DOMPurify from "dompurify";
import { formatDate } from "../../util/dateUtil";

interface Props {
    keycloak: Keycloak | null;
    keycloakStatus: "loading" | "authenticated" | "unauthenticated";
}

interface ContentData {
    id: string;
    content: string;
    createdDate: string;
    nickname: string;
    postUuid: string;
    title: string;
    updatedDate: number;
    userUuid: string;
    commentCount: number;
    likeCount:number;
    totalViews:number;
}

interface NicknamePostsData{
    postUuid: string;
    userUuid: string;
    nickname: string;
    title: string;
}

const BlogSearchPage : React.FC<Props> = ({keycloak, keycloakStatus}) => {
    const { nickname } = useParams();
    const [searchParams, setSearchParams] = useSearchParams();
    const keyword = searchParams.get('keyword');
    const [inputValue, setInputValue] = useState(keyword || "");
    const inputRef = useRef<HTMLInputElement>(null);

    const [totalElements, setTotalElements] = useState<number>(0);
    const [contentData, setContentData] = useState<ContentData[]>([]);
    const [pageEmpty, setPageEmpty] = useState<boolean>(false);
    const [page, setPage] = useState<number>(0);
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const [hasError, setHasError] = useState<boolean>(false);
    const [sentinel, setSentinel] = useState<HTMLDivElement | null>(null);

    const [blogPosts, setBlogPosts] = useState<NicknamePostsData[]>([]);
    const [blogTitle, setBlogTitle] = useState<string>('');
    const [blogIntro, setBlogIntro] = useState<string>('');

    const [showRemote, setShowRemote] = useState<boolean>(true);
    const [showSlide, setShowSlide] = useState<boolean>(false);

    const { toPostDetail, toUserBlog } = useNavigationHelper();

    useEffect(() => {
        console.log("1번 @");
        setContentData([]);
        setPage(0);
        setTotalElements(0)
        setPageEmpty(false);
        setHasError(false);

    },[nickname, keyword]);

    useEffect(() => {
        if (nickname && keyword?.length) {
            console.log("2번 @");
            fetchSearchUserPosts(page);
        }
    }, [nickname, keyword, page]);

    useEffect(() => {
        if(!nickname) return;
        console.log("3번 @");
        fetchNicknamePosts(nickname);
        fetchNicknameProfile(nickname);
    }, [nickname]);


    useEffect(() => {
        
        if (!sentinel) {
            console.warn("Sentinel이 DOM에 연결되지 않았습니다.");
            return;
        }

        if(pageEmpty) {
            console.warn(`pageEmpty : ${pageEmpty}`);
            return;
        }

        const observer = new IntersectionObserver(
            (entries) => {
                console.log("감지하고 있나?");
                entries.forEach((entry) => {
                    if (entry.isIntersecting && !isLoading && !hasError) {
                        setPage((prevPage) => prevPage + 1);
                        console.log("스크롤이 맨 아래에 닿기 직전입니다!", page + 1);
                    }
                });
            },
            { root: null, rootMargin: "0px", threshold: 1.0 }
        );
    
        observer.observe(sentinel);
    
        return () => {
            observer.disconnect();
        };
    }, [sentinel, isLoading, hasError]);



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

    const fetchSearchUserPosts = (currentPage: number) => {
        // (keyword?.length && contentData.length === 0 && pageEmpty)
        console.log(`검색 - keyword.length: ${keyword?.length}, data.length: ${contentData.length}, pageEmpty: ${pageEmpty}`);
        if (pageEmpty || isLoading) return;
        console.log(`데이터 요청시작`);
        setIsLoading(true);
        setHasError(false);

        searchNicknamePostsByText(nickname!, keyword!, currentPage, 20)
            .then((response) => {
                console.log('어떻게오는데 :', response);
                console.log('비어있는거 확인 @  :', response.empty);
                setTotalElements(response.totalElements)
                setContentData((prevData) => [...prevData, ...response.content]);
                setPageEmpty(response.empty);
            })
            .catch((error) => {
                console.error('Error fetching data:', error);
                setHasError(true);
            })
            .finally(() => {
                setIsLoading(false); // 로딩 종료
            });
        
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

    const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
        if (e.key === 'Enter') {
            // 검색어가 존재하면 쿼리 파라미터 추가
            if (inputValue.trim() !== "") {
                setContentData([]);
                setPage(0);
                setPageEmpty(false);
                setHasError(false);
                setTotalElements(0)
                setSearchParams({ keyword: inputValue.trim() });
            } else {
                // 검색어가 비어있으면 쿼리 파라미터를 제거하고 기본 주소로
                setSearchParams({});
            }
        }
    };
    const toggleSlide = () => {
        setShowSlide((prev) => !prev);
    };

    if (!keycloak) {
        return (
            <>
                <SkeletonCommonHeader/>
                <SkeletonBlogSearchBody/>
            </> 
        )
    }

    return (
        <>
            <Header keycloak={keycloak} keycloakStatus={keycloakStatus} />
            {/* 전체 바디(빈공간포함) */}
            <div className="min-w-full min-h-screen flex justify-center  bg-gray-100">
                {/* 본문(빈공간 미포함) */}
                <div className="max-w-1728 2xl:max-w-1376 xl:max-w-1024  flex-1 flex justify-center bg-white shadow-lg">

                    {/* 검색 섹션 */}
                    <div className="relative pt-32 pb-16 w-[768px] xs:w-full xs:px-4 bg-white flex flex-col">
                        {/* 왼쪽 게시글 목록 */}
                        <aside className="absolute top-0 -left-72 w-64 min-h-72 flex h-full xl:hidden bg-white">
                            {/* Inner Box */}
                            <div className="mt-40 mb-10 py-4 w-full max-h-full flex flex-col rounded-md">
                                <div // 블로그 이름
                                    className="px-2 py-4 text-xl bg-gray-100 rounded-md mb-2 text-center font-bold cursor-pointer"
                                    onClick={() => toUserBlog(nickname!)}>
                                        {blogTitle}
                                </div>
                                <div // 블로그 소개
                                    className="px-2 py-4 rounded-md border border-gray-300 text-sm bg-white  empty:hidden break-words whitespace-pre-line">
                                    {blogIntro}
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

                        {/* 검색 기준유저 명 */}
                        <div className="mt-4 mb-2 font-semibold">
                            {`'${nickname}' 님이 작성한 게시글 검색`}
                        </div>
                        {/* 검색 인풋 */}
                        <div 
                            className="px-6 xs:px-4 py-1 h-16 xs:h-10 mb-6 flex items-center border  cursor-text border-black w-full bg-white focus-within:border-gray-400"
                            onClick={() => inputRef.current?.focus()}>
                            <BsSearch className="mr-5 text-3xl xs:text-lg xs:mr-3"/>
                            <input
                                ref={inputRef} 
                                className="h-8 text-2xl xs:text-lg w-full outline-none"
                                placeholder="검색어를 입력하세요"
                                type="text"
                                value={inputValue}
                                onChange={(e) => setInputValue(e.target.value)}
                                onKeyDown={handleKeyDown} />
                        </div>
                        {keyword?.length ? (
                            <div className="text-lg xs:text-base mb-16">
                                총 <b>{totalElements}개</b>의 게시글을 찾았습니다
                            </div>
                        ) : (
                            <div className="text-lg xs:text-base mb-16">
                                검색어를 올바르게 입력해주세요
                            </div>
                        )}
                        <div className="flex flex-col gap-16">
                            {contentData.map((item) => {
                                // 0) 본문 콘텐츠를 정제
                                const sanitizedContent = DOMPurify.sanitize(item.content);
    
                                // 1) 임시 div 생성 후, sanitize 적용
                                const tempElement = document.createElement("div");
                                tempElement.innerHTML = sanitizedContent;
            
                                // 2) 모든 <img> 태그를 가져옴
                                const imgElements = Array.from(tempElement.querySelectorAll("img"));
            
                                // 3) 대표 이미지 URL 추출 (첫 번째 <img> 기준)
                                let imageUrl: string | null = null;
                                if (imgElements.length > 0) {
                                imageUrl = imgElements[0].src;
                                }
    
                                // 4) 나머지 이미지를 전부 제거
                                //    (대표 이미지도 본문에선 제거해야 하므로, 전체 <img> 태그 remove)
                                imgElements.forEach((img) => {
                                    img.remove();
                                });
                                return (
                                    <div 
                                        key={item.postUuid}
                                        className=" flex flex-col w-full h-auto overflow-hidden rounded-lg shadow-lg hover:shadow-xl transform hover:-translate-y-2 transition duration-300 ease-in-out ">

                                        {imageUrl && (
                                            <div className=" max-h-[400px] overflow-hidden cursor-pointer bg-red-300"
                                                onClick={() => toPostDetail(item.nickname, item.postUuid)}>
                                                <img
                                                    src={imageUrl}
                                                    alt="content"
                                                    className="h-full w-full object-cover"
                                                />
                                            </div>
                                        )}
                                        
                                        <div className="px-2 my-6 xs:my-2  text-3xl xs:text-lg font-black line-clamp-2 cursor-pointer"
                                            onClick={() => toPostDetail(item.nickname, item.postUuid)}>
                                            {item.title}
                                        </div>
                                        <div className="px-2 xs:text-sm line-clamp-3 xs:line-clamp-4 mb-10 cursor-pointer "dangerouslySetInnerHTML={{ __html: tempElement.innerHTML }} onClick={() => toUserBlog(item.nickname)}></div>
                                        <div className=" w-full flex items-center px-4 py-2 border-t border-gray-200 text-gray-500 text-sm
                                                2xs:h-auto 2xs:text-xs">
                                            <div className="flex bg-white  gap-2">
                                                <div>
                                                    {formatDate(item.createdDate)}
                                                </div>
                                                <div className="flex items-center gap-1">
                                                    <FaRegComment size={15} />
                                                    <div>{item.totalViews}</div>
                                                </div>
                                                <div className="flex items-center gap-1">
                                                    <PiEyeLight size={17}/> 
                                                    <div>{item.likeCount}</div>
                                                </div>
                                                <div className="flex items-center gap-1">
                                                    <FaHeart size={14}/>
                                                    <div>{item.commentCount}</div>
                                                </div>
                                            </div>
                                        </div>

                                    </div>

                                );
                            })}
                        </div>
                        {/* 빈 리스트 */}
                        {(keyword?.length && contentData.length === 0 && pageEmpty) && (
                            <div className="flex flex-col justify-center items-center">
                                <div className="w-[425px] 2xs:w-[325px] ">
                                    <img
                                        className="ml-4" 
                                        src="https://images.ghtjr.com/e94915c9-e28d-4217-ad2e-e30a7a685d7c_empty_box_miniblog.png" alt="" />
                                </div>
                                <div className="mt-6 text-2xl 2xs:text-xl text-gray-500">
                                    검색 결과가 없습니다
                                </div>
                            </div>
                        )}
                    </div>

                </div>
            </div>
            <div ref={setSentinel} className="h-4"></div>
            {/* 리모콘 */}
            {showRemote && (
                <aside className="fixed flex flex-col gap-4 top-[calc(50%-96px)] right-[calc(50%-468px)] sm:right-2">
                    <button // 게시글 목록 슬라이드
                        className=" w-10 h-10 bg-gray-700 hover:bg-gray-500 text-white rounded-md hidden xl:flex items-center justify-center"
                        onClick={toggleSlide}
                    >
                        <FaAlignJustify size={25} />
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
                <div className="px-4 pt-4 text-base font-bold">
                    게시글 목록
                </div>
                <ul className="px-4 py-8 space-y-4  overflow-y-auto">
                    {blogPosts.map((item) => (
                        <li 
                            key={item.postUuid}
                            className="hover-text-blink px-2 text-sm truncate cursor-pointer"
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
        </>
    )
}

export default BlogSearchPage;