import React, { useEffect, useState } from "react"; 
import Keycloak from "keycloak-js";
import './../../styles/image-hidden.css';
import SkeletonHomeHeader from "../../components/skeleton/SkeletonHomeHeader";
import SkeletonCardBody from "../../components/skeleton/SkeletonCardBody";
import HomeHeader from "../../components/header/home/HomeHeader";
import { PiEyeLight } from "react-icons/pi";
import { FaHeart, FaRegComment } from "react-icons/fa";
import { useLocation } from "react-router-dom";
import { getLatestPosts, getMostLikedPosts, getMostViewedPosts } from "../../service/queryService.public";
import DOMPurify from "dompurify";
import useNavigationHelper from "../../util/navigationUtil";
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

type SortOption = 'views' | 'latest' | 'likes' | null;

const TestHomePage: React.FC<Props> = ({ keycloak, keycloakStatus }) => {
    const location = useLocation();
    const [selectedSort, setSelectedSort] = useState<SortOption>(null);
    const [contentData, setContentData] = useState<ContentData[]>([]);
    const [pageEmpty, setPageEmpty] = useState<boolean>(false);
    const [page, setPage] = useState<number>(0);
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const [hasError, setHasError] = useState<boolean>(false);
    const [sentinel, setSentinel] = useState<HTMLDivElement | null>(null);

    const { toPostDetail, toUserBlog } = useNavigationHelper();

    useEffect(() => {
        if (location.pathname === "/latest") {
            setSelectedSort('latest')
        } else if (location.pathname === "/likes") {
            setSelectedSort('likes')
        } else {
            setSelectedSort('views')
        }
        setContentData([]);
        setPage(0);
        setPageEmpty(false);
        setHasError(false);

        console.log(`첫번째 로직 : ${location.pathname}, ${selectedSort}`)
    }, [location.pathname]);

    useEffect(() => {
        fetchData(page);
        console.log(`두번째 로직 : 정렬 = ${selectedSort}, 페이지 = ${page}`)
    }, [selectedSort, page]);

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

    const fetchData = (currentPage: number) => {
        
        if ( !selectedSort || pageEmpty || isLoading) return; // 페이지 끝이거나 로딩 중이면 반환

        setIsLoading(true);
        setHasError(false);

        let fetchPromise;
        console.log(`fetchData switch문 전 : ${selectedSort}`);
        switch (selectedSort) {
            case 'latest':
                fetchPromise = getLatestPosts(currentPage, 40);
                break;
            case 'likes':
                fetchPromise = getMostLikedPosts(currentPage, 40);
                break;
            default:
                fetchPromise = getMostViewedPosts(currentPage, 40);
        }

        fetchPromise
            .then((response) => {
                console.log('Fetched data:', response.content, response.empty);
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
    };

    if (!keycloak) {
        return (
            <>
                <SkeletonHomeHeader/>
                <SkeletonCardBody/>
            </>
        )
    }

    return (
        <>
            <HomeHeader keycloak={keycloak} keycloakStatus={keycloakStatus}/>
            {/* 전체 바디 (빈공간 포함) */}
            <div className="min-w-full min-h-screen flex justify-center bg-gray-100">
                {/* 본문 (빈공간 미포함) */}
                <div className="flex-1 max-w-1728 pt-40 pb-4 px-4 2xl:max-w-1376 xl:max-w-1024 ">
                    {/* 본문 그리드 */}
                    <div className=" grid grid-cols-5 gap-6 mx-auto
                        2xl:max-w-1376 2xl:grid-cols-4
                        xl:max-w-1024 xl:grid-cols-3 
                        md:grid-cols-2 
                        xs:flex xs:flex-col">
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
                                    className="bg-white w-full aspect-[17/20] md:aspect-[20/19] xs:aspect-[20/16] flex flex-col rounded-lg shadow-lg overflow-hidden hover:shadow-xl transform hover:-translate-y-2 transition duration-300 ease-in-out">
                                    
                                    {imageUrl && (
                                        <div 
                                            className="h-[167px] overflow-hidden cursor-pointer md:h-[255px] xs:h-[372px]"
                                            onClick={() => toPostDetail(item.nickname, item.postUuid)}>

                                            <img
                                                src={imageUrl}
                                                alt="content"
                                                className="h-full w-full object-cover"
                                            />
                                        </div>
                                    )}
                                    <div 
                                        className="flex-1 flex-col p-4 cursor-pointer"
                                        onClick={() => toPostDetail(item.nickname, item.postUuid)}>
                                        <div className=" text-lg mb-1 font-black line-clamp-1">
                                            {item.title}
                                        </div>
                                        <div 
                                            className="text-sm text-gray-600 line-clamp-3"
                                            dangerouslySetInnerHTML={{ __html: tempElement.innerHTML }}>
                                        </div>
                                    </div>
                                    <div className=" flex flex-col justify-end text-gray-500">
                                        <div className="px-4 py-2.5 text-xs">
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
                    {contentData.length === 0 && (
                        <div className="flex flex-col justify-center items-center">
                            <div className="w-[425px] 2xs:w-[325px] ">
                                <img
                                    className="ml-4" 
                                    src="https://images.ghtjr.com/e94915c9-e28d-4217-ad2e-e30a7a685d7c_empty_box_miniblog.png" alt="" />
                            </div>
                            <div className="mt-6 text-2xl 2xs:text-xl text-gray-500">
                                리스트가 비어있습니다.
                            </div>
                        </div>
                    )}
                </div>
                
            </div>
            <div ref={setSentinel} className="h-4"></div>
            
        </>
    )

};

export default TestHomePage;