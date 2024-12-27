import React, { useEffect, useRef, useState } from "react"; 
import Keycloak from "keycloak-js";
import { useSearchParams } from "react-router-dom";
import Header from "../../components/header/common/Header";
import { BsSearch } from "react-icons/bs";
import { FaHeart, FaRegComment } from "react-icons/fa";
import { PiEyeLight } from "react-icons/pi";
import { TiArrowDownThick, TiArrowUpThick } from "react-icons/ti";
import SkeletonCommonHeader from "../../components/skeleton/SkeletonCommonHeader";
import SkeletonSearchBody from "../../components/skeleton/SkeletonSearchBody";
import useNavigationHelper from "../../util/navigationUtil";
import { searchPostsByText } from "../../service/queryService.public";
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

const SearchPage : React.FC<Props> = ({keycloak, keycloakStatus}) => {
    const [searchParams, setSearchParams] = useSearchParams();
    const keyword = searchParams.get('keyword');
    const [inputValue, setInputValue] = useState(keyword || "");
    const inputRef = useRef<HTMLInputElement>(null);
    const [showRemote, setShowRemote] = useState<boolean>(true);

    const [contentData, setContentData] = useState<ContentData[]>([]);
    const [pageEmpty, setPageEmpty] = useState<boolean>(false);
    const [page, setPage] = useState<number>(0);
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const [hasError, setHasError] = useState<boolean>(false);
    const [sentinel, setSentinel] = useState<HTMLDivElement | null>(null);

    const { toPostDetail, toUserBlog } = useNavigationHelper();

    useEffect(() => {

        if (keyword?.length) {
            console.log("키워드는? : " + keyword)
            fetchSearchAllPosts(page);
        }
    }, [keyword, page]);

    useEffect(() => {
        setContentData([]);
        setPage(0);
        setPageEmpty(false);
        setHasError(false);

    },[keyword]);

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

    const fetchSearchAllPosts = (currentPage: number) => {
        if (pageEmpty || isLoading) return;

        setIsLoading(true);
        setHasError(false);

        searchPostsByText(keyword!, currentPage, 20)
            .then((response) => {
                console.log('어떻게오는데 :', response);
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

    const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
        if (e.key === 'Enter') {
            // 검색어가 존재하면 쿼리 파라미터 추가
            if (inputValue.trim() !== "") {
                setSearchParams({ keyword: inputValue.trim() });
            } else {
                // 검색어가 비어있으면 쿼리 파라미터를 제거하고 기본 주소로
                setSearchParams({});
            }
        }
    };

    if (!keycloak) {
        return (
            <>
                <SkeletonCommonHeader/>
                <SkeletonSearchBody/>
            </> 
        )
    }

    return (
        <>
            <Header keycloak={keycloak} keycloakStatus={keycloakStatus}/>
            {/* 전체 바디(빈공간포함) */}
            <div className="min-w-full min-h-screen flex justify-center  bg-gray-100">
                {/* 본문(빈공간 미포함) */}
                <div className="max-w-1728 2xl:max-w-1376 xl:max-w-1024  flex-1 flex justify-center bg-white shadow-lg">
                    {/* 검색 섹션 */}
                    <div className="relative pt-32 pb-16 w-[768px] xs:w-full xs:px-4  2xs:px-2 bg-white flex flex-col">
                        {/* 검색 인풋 */}
                        <div 
                            className="px-6 xs:px-4 py-1 h-16 xs:h-10 mb-6 flex items-center border border-black w-full cursor-text bg-white focus-within:border-gray-400"
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
                        {keyword?.length && (
                            <div className="text-lg xs:text-base mb-16">
                                총 <b>{contentData.length}개</b>의 게시글을 찾았습니다
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

                                return(
                                    <div 
                                        key={item.postUuid}
                                        className=" flex flex-col w-full h-auto overflow-hidden rounded-lg shadow-lg hover:shadow-xl transform hover:-translate-y-2 transition duration-300 ease-in-out ">
                                        {imageUrl && (
                                            <div className=" max-h-[400px] overflow-hidden cursor-pointer bg-red-300">
                                                <img
                                                    src={imageUrl}
                                                    alt="content"
                                                    className="h-full w-full object-cover"
                                                />
                                            </div>
                                        )}        
                                        <div className="px-2 my-6 xs:my-2  text-2xl xs:text-lg font-black line-clamp-2 cursor-pointer"
                                            onClick={() => toPostDetail(item.nickname, item.postUuid)}>
                                            {item.title}
                                        </div>
                                        <div className="px-2 xs:text-sm line-clamp-3 xs:line-clamp-4 mb-6 cursor-pointer"dangerouslySetInnerHTML={{ __html: tempElement.innerHTML }} onClick={() => toPostDetail(item.nickname, item.postUuid)}></div>
                                        <div className=" w-full flex flex-col text-gray-500
                                                2xs:h-auto 2xs:text-xs">
                                            <div className="px-4 py-2.5 text-xs ">
                                                {formatDate(item.createdDate)}
                                            </div>
                                            <div className="px-4 py-2.5 flex justify-between text-xs border-t border-gray-200 text-black ">
                                                <b className="text-black cursor-pointer" onClick={() => toUserBlog(item.nickname)}>{item.nickname}</b>
                                                <div className="flex gap-2">
                                                    <div className="flex items-center gap-1">
                                                        <PiEyeLight size={17}/> 
                                                        <div>{item.totalViews}</div>
                                                    </div>
                                                    <div className="flex items-center gap-1">
                                                        <FaHeart size={14}/>
                                                        <div>{item.likeCount}</div>
                                                    </div>
                                                    <div className="flex items-center gap-1">
                                                        <FaRegComment size={15} />
                                                        <div>{item.commentCount}</div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                );
                            })}
                        </div>
                        {/* 빈 리스트 */}
                        {(contentData.length === 0 && keyword?.length ) && (
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

        </>
    );
}

export default SearchPage;