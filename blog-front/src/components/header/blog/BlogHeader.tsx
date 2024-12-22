import React, { useEffect, useState } from "react";
import Keycloak from 'keycloak-js';
import { IoMdSearch } from "react-icons/io";
import { FiUser } from "react-icons/fi";
import useNavigationHelper from "../../../util/navigationUtil";
import { useLocation } from "react-router-dom";
import { CiClock2, CiHeart, CiWavePulse1 } from "react-icons/ci";

interface Props{
    keycloak: Keycloak | null;
    nickname: string;
}

type SortOption = 'views' | 'latest' | 'likes';

const BlogHeader: React.FC<Props> = ({keycloak, nickname}) => {
    const location = useLocation();
    const [selectedSort, setSelectedSort] = useState<SortOption>('views');
    const [dropdownVisible, setDropdownVisible] = useState<boolean>(false);
    const [isVisible, setIsVisible] = useState<boolean>(true);
    const { toHome, toUserBlogViews, toUserBlogLatest, toUserBlogLikes, toSearch, toHistory, toPostWrite, toSetting} = useNavigationHelper();

    let lastScrollY = window.scrollY;

    useEffect(() => {
        // 경로를 '/'로 나눠 배열로 만듦
        const pathSegments = location.pathname.split("/");
        // 마지막 경로 추출
        const lastSegment = pathSegments[pathSegments.length - 1];
        if (lastSegment === "latest") {
            console.log(lastSegment)
            setSelectedSort('latest')
        } else if (lastSegment === "likes") {
            console.log(lastSegment)
            setSelectedSort('likes')
        } else {
            console.log(lastSegment)
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

    // 드롭다운 외부 클릭 감지
    useEffect(() => {
        const handleClickOutside = (e: MouseEvent) => {
            if (!(e.target as HTMLElement).closest(".dropdown-container")) {
                setDropdownVisible(false);
            }
        };
        document.addEventListener("click", handleClickOutside);

        return () => {
        document.removeEventListener("click", handleClickOutside);
        };
    }, []);

    // 데이터 정렬 변경
    const handleSortChange = (option: SortOption) => {
        setSelectedSort(option);
        if (option === 'views') {
            toUserBlogViews(nickname)
        } else if (option === 'latest') {
            toUserBlogLatest(nickname)
        } else if (option === 'likes') {
            toUserBlogLikes(nickname)
        }
    };

    // 정렬 버튼 동적 스타일
    const getButtonClasses = (option: SortOption) => {
        const buttonBaseClasses = "flex items-center gap-2 cursor-pointer remote-button px-4";
        return selectedSort === option
            ? `${buttonBaseClasses} border-b-2 border-black text-black`
            : `${buttonBaseClasses} text-gray-500 hover:text-black`;
    };

    // 드랍다운 토글
    const toggleDropdown = () => {
        setDropdownVisible(!dropdownVisible);
    }

    const handleSignin = () => {
        keycloak?.login();
    };

    const handleLogout = () => {
        keycloak?.logout();
    };

    return (
        <div className={`fixed top-0 z-10 w-full flex justify-center shadow-md bg-white
            transition-transform duration-300 
            ${isVisible ? "translate-y-0" : "-translate-y-full"}`}>
            <div className="max-w-1728 2xl:max-w-1376 xl:max-w-1024 md:mx-4 w-full flex flex-col items-center justify-center  ">
                <div className="w-full h-16 flex items-center justify-between ">
                    <div 
                        className="text-2xl md:text-xl font-bold cursor-pointer  "
                        onClick={toHome}
                    >
                            <div className="xs:hidden">Miniblog</div>
                        <div className="h-12 w-12 hidden xs:flex overflow-hidden">
                            <img 
                            className="h-full w-full object-cover"
                            src="/miniblog_48x48.png" alt="" />
                        </div>
                    </div>
                    <div className="flex flex-row gap-2 items-center">
                        <div 
                            className="rounded-full cursor-pointer hover:bg-gray-100 p-2"
                            onClick={toSearch}
                        >
                            <IoMdSearch size={32}/>
                        </div>
                        {/* 로그인 상태 분기점 */}
                        {keycloak?.authenticated ? (
                            <div 
                                className="remote-button rounded-full cursor-pointer  flex justify-center items-center group relative dropdown-container"
                                onClick={toggleDropdown}
                            >
                                <FiUser size={32}/>
                                <div className=" text-xs text-gray-400 group-hover:text-black">▼</div>
                                {/* (로그인) 드랍다운 */}
                                {dropdownVisible && (
                                    <div className="absolute top-14 right-0 w-48 bg-white shadow-header-dropdown-shadow z-10 rounded-md overflow-hidden">
                                        <div className="px-3 py-4 text-gray-500 hover:text-black hover:font-semibold hover:bg-gray-100">
                                            내 블로그
                                        </div>
                                        <div 
                                            className="px-3 py-4 text-gray-500 hover:text-black hover:font-semibold hover:bg-gray-100"
                                            onClick={toPostWrite}
                                        >
                                            게시글 작성
                                        </div>
                                        <div 
                                            className="px-3 py-4 text-gray-500 hover:text-black hover:font-semibold hover:bg-gray-100"
                                            onClick={toHistory}
                                        >
                                            히스토리
                                        </div>
                                        <div 
                                            className="px-3 py-4 text-gray-500 hover:text-black hover:font-semibold hover:bg-gray-100"
                                            onClick={toSetting}
                                        >
                                            설정
                                        </div>
                                        <div 
                                            className="px-3 py-4 text-gray-500 hover:text-black hover:font-semibold hover:bg-gray-100"
                                            onClick={handleLogout}
                                        >
                                            로그아웃
                                        </div>
                                    </div>
                                )}
                            </div>
                        ) : (
                            <div
                                className="px-4 h-8 rounded-2xl border border-black text-lg flex items-center bg-white hover:bg-black text-black hover:text-white font-bold cursor-pointer transition-colors duration-300 ease-in-out"
                                onClick={handleSignin}
                            >
                                로그인
                            </div>
                        )}
                    </div>
                </div>
                <div className="max-w-768 w-full h-12 flex font-bold gap-2 ">
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
            </div>
        </div>
    );
}

export default BlogHeader;