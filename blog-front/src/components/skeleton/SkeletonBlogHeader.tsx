import { useEffect, useState } from "react";
import { CiClock2, CiHeart, CiWavePulse1 } from "react-icons/ci";
import { useLocation } from "react-router-dom";

type SortOption = 'views' | 'latest' | 'likes';

const SkeletonBlogHeader = () => {
    const location = useLocation();
    const [selectedSort, setSelectedSort] = useState<SortOption>('views');
    const [isVisible, setIsVisible] = useState<boolean>(true);

    let lastScrollY = window.scrollY;

    useEffect(() => {
        // 경로를 '/'로 나눠 배열로 만듦
        const pathSegments = location.pathname.split("/");
        // 마지막 경로 추출
        const lastSegment = pathSegments[pathSegments.length - 1];
        if (lastSegment === "latest") {
            setSelectedSort('latest')
        } else if (lastSegment === "likes") {
            setSelectedSort('likes')
        } else {
            setSelectedSort('views')
        }
    }, [location.pathname]);

    // 스크롤 이벤트 (헤더)
    useEffect(() => {
        const handleScroll = () => {
            if (window.scrollY > lastScrollY) {
                // 스크롤을 내리는 중: 헤더 숨기기
                setIsVisible(false);
            } else {
                // 스크롤을 올리는 중: 헤더 보이기
                setIsVisible(true);
            }
            lastScrollY = window.scrollY;
        };
        // 스크롤 이벤트 리스너 추가
        window.addEventListener("scroll", handleScroll);

        // 컴포넌트 언마운트 시 리스너 제거
        return () => {
            window.removeEventListener("scroll", handleScroll);
        };
    }, []);

    // 정렬 버튼 동적 스타일
    const getButtonClasses = (option: SortOption) => {
        const buttonBaseClasses = "flex items-center gap-2 remote-button px-4";
        return selectedSort === option
            ? `${buttonBaseClasses} border-b-2 border-black text-black`
            : `${buttonBaseClasses} text-gray-500 `;
    };

    // 새로고침
    const handleRefresh = () => {
        window.location.reload();
    };

    return (
        <div className={`fixed top-0 z-10 w-full flex justify-center shadow-md bg-white
                        transition-transform duration-300 
                        ${isVisible ? "translate-y-0" : "-translate-y-full"}`}>
            <div className="max-w-1728 2xl:max-w-1376 xl:max-w-1024 md:mx-4 w-full flex flex-col items-center justify-center">
                <div className="w-full h-16 flex items-center justify-between ">
                    <div className="text-2xl md:text-xl font-bold cursor-pointer " onClick={handleRefresh}>
                        <div className="xs:hidden">Miniblog</div>
                        <div className="h-12 w-12 hidden xs:flex overflow-hidden">
                            <img 
                            className="h-full w-full object-cover"
                            src="/miniblog_48x48.png" alt="" />
                        </div>
                    </div>
                    <div className="flex flex-row gap-4 items-center">
                        <div className="bg-gray-300 rounded-full h-12 w-12 animate-pulse" />
                        <div className="bg-gray-300 rounded-full h-12 w-12 animate-pulse" />
                    </div>
                </div>
                <div className="max-w-768 w-full h-12 flex font-bold gap-2 ">
                    <div className={getButtonClasses('views')} >
                        <CiWavePulse1 className="text-2xl" />
                        조회수
                    </div>
                    <div className={getButtonClasses('latest')}>
                        <CiClock2 className="text-2xl" />
                        최신
                    </div>
                    <div className={getButtonClasses('likes')}>
                        <CiHeart className="text-2xl" />
                        좋아요
                    </div>
                </div>
            </div>
        </div>
    );
}

export default SkeletonBlogHeader;