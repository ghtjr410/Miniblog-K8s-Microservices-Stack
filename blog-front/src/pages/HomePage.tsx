import React, { useEffect, useRef, useState } from "react"; 
import TestHeader from "../components/header/TestHeader";
import Keycloak from "keycloak-js";
import { CiWavePulse1, CiClock2, CiHeart } from "react-icons/ci";
import axios from "axios";
import { API_QUERY_URL } from "../util/apiUrl";
import DOMPurify from 'dompurify';
import { getLatestPosts, getMostLikedPosts, getMostViewedPosts } from "../service/queryService.public";
import './../styles/image-hidden.css';
import { formatDate } from "../util/dateUtil";

interface Props{
    keycloak: Keycloak | null;
}
type SortOption = 'views' | 'latest' | 'likes';

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
}


const HomePage: React.FC<Props> = ({ keycloak }) => {
    const [selectedSort, setSelectedSort] = useState<SortOption>('views');
    const [contentData, setContentData] = useState<ContentData[]>([]);
    const [pageEmpty, setPageEmpty] = useState<boolean>(false);
    const [page, setPage] = useState<number>(0);
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const [hasError, setHasError] = useState<boolean>(false);
    const sentinelRef = useRef<HTMLDivElement | null>(null);

    const handleSortChange = (option: SortOption) => {
        setSelectedSort(option);
        setPage(0);
        setPageEmpty(false);
        setContentData([]);
        setHasError(false);
    };

    const buttonBaseClasses = "flex items-center gap-2 cursor-pointer";
    const getButtonClasses = (option: SortOption) => {
        return selectedSort === option
            ? `${buttonBaseClasses} border-b-2 border-black text-black`
            : `${buttonBaseClasses} text-gray-500 hover:text-black`;
    };

    const fetchData = (currentPage: number) => {
        if (pageEmpty || isLoading) return; // 페이지 끝이거나 로딩 중이면 반환

        setIsLoading(true);
        setHasError(false);

        let fetchPromise;

        if (selectedSort === 'latest') {
            fetchPromise = getLatestPosts(currentPage, 40);
        } else if (selectedSort === 'likes') {
            fetchPromise = getMostLikedPosts(currentPage, 40);
        } else {
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

    useEffect(() => {
        fetchData(page);
    }, [selectedSort, page]);

    useEffect(() => {
        const observer = new IntersectionObserver(
            (entries) => {
                entries.forEach((entry) => {
                    if (entry.isIntersecting && !isLoading && !hasError) {
                        setPage((prevPage) => prevPage + 1);
                        console.log('스크롤이 맨 아래에 닿기 직전입니다!', page + 1);
                    }
                });
            },
            { root: null, rootMargin: '0px', threshold: 1.0 }
        );

        if (sentinelRef.current) {
            observer.observe(sentinelRef.current);
        }

        return () => {
            if (sentinelRef.current) {
                observer.unobserve(sentinelRef.current);
            }
        };
    }, [contentData, isLoading, hasError]);



    return (
        <div className="flex flex-col h-full">
            <TestHeader keycloak={keycloak} />

            <div className="flex flex-row min-h-0 gap-2 max-w-screen-2xl mx-auto w-full shadow-custom-default text-lg font-bold items-center p-4">
                <div className={getButtonClasses('views')} onClick={() => handleSortChange('views')}>
                    <CiWavePulse1 className="text-2xl" />
                    조회수
                </div>
                <div className={getButtonClasses('latest')} onClick={() => handleSortChange('latest')}>
                    <CiClock2 className="text-2xl" />
                    최신
                </div>
                <div className={getButtonClasses('likes')} onClick={() => handleSortChange('likes')}>
                    <CiHeart className="text-2xl" />
                    좋아요
                </div>
            </div>

            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4 p-4 max-w-screen-2xl mx-auto w-full shadow-custom-default">
                {contentData.map((item) => {
                    // 본문 콘텐츠를 정제
                    const sanitizedContent = DOMPurify.sanitize(item.content);

                    // 임시 div에 콘텐츠 삽입 후 첫 번째 이미지 태그를 찾기
                    const tempElement = document.createElement("div");
                    tempElement.innerHTML = sanitizedContent;

                    // 첫 번째 이미지 태그 찾기
                    const imgElement = tempElement.querySelector("img");

                    // 첫 번째 이미지의 URL 추출
                    const imageUrl = imgElement ? imgElement.src : null;


                    return (
                        <div
                            key={item.id}
                            className="border rounded-lg shadow-lg hover:shadow-xl transform hover:-translate-y-2 transition duration-300 ease-in-out h-96 w-80 flex flex-col justify-between overflow-hidden"
                        >
                            {/* 이미지 URL이 있으면 상단에 이미지 표시 */}
                            {imageUrl && (
                                <div 
                                    className="w-full h-40 overflow-hidden cursor-pointer"
                                    onClick={() => alert(`Post UUID: ${item.postUuid}`)}
                                >
                                    <img
                                        src={imageUrl}
                                        alt="content"
                                        className="h-full w-full object-cover"
                                    />
                                </div>
                            )}
                            <div 
                                className="m-4 cursor-pointer"
                                onClick={() => alert(`Post UUID: ${item.postUuid}`)}
                            >
                                <div className="mb-1 text-base font-bold truncate">
                                    {item.title}
                                </div>
                                <div 
                                className="image-hidden line-clamp-3"
                                dangerouslySetInnerHTML={{ __html: tempElement.innerHTML }}
                                >
                                </div>
                            </div>
                            <div className="mt-auto ">
                                <div>
                                    <div className="mt-auto flex px-4 py-2.5 text-xs gap-1">
                                        <div>{formatDate(item.createdDate)}</div>
                                        <div>·</div>
                                        <div>{item.commentCount}개의 댓글</div>
                                    </div>
                                </div>
                                <div className="border-t">
                                    <div className="flex justify-between px-4 py-2.5 text-xs">
                                        <div
                                            className="cursor-pointer" 
                                            onClick={() => alert(`User UUID: ${item.userUuid}`)}
                                        >
                                            {item.nickname}
                                        </div>
                                        <div>♥ {item.likeCount}</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    );
                })}
            </div>

            <div ref={sentinelRef} className="h-4"></div>

            {isLoading && <div className="text-center p-4">Loading...</div>}
            {hasError && <div className="text-center p-4 text-red-500">데이터를 불러오는 중 오류가 발생했습니다.</div>}
        </div>
        
    );
};

export default HomePage;