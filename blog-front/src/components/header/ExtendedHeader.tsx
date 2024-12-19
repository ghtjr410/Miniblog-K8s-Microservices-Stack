import React, { useEffect, useState } from "react";
import Keycloak from 'keycloak-js';
import { CiBellOn, CiClock2, CiHeart, CiWavePulse1 } from "react-icons/ci";
import { CiSearch } from "react-icons/ci";
import { CiUser } from "react-icons/ci";
import { IoMdSearch } from "react-icons/io";
import { FaRegUserCircle } from "react-icons/fa";
import { FiUser } from "react-icons/fi";
import { TbUserDown } from "react-icons/tb";
import useNavigationHelper from "../../util/navigationUtil";

interface Props{
    keycloak: Keycloak | null;
    selectedSort: SortOption;
    onSortChange: (option: SortOption) => void;
}
type SortOption = 'views' | 'latest' | 'likes';

const ExtendedHeader: React.FC<Props> = ({keycloak, selectedSort, onSortChange}) => {
    const [dropdownVisible, setDropdownVisible] = useState<boolean>(false);
    const [isVisible, setIsVisible] = useState<boolean>(true);
    const { toHome, toPostWrite, toSetting} = useNavigationHelper();

    let lastScrollY = window.scrollY;
    useEffect(() => {
        // 스크롤 이벤트 리스너 추가
        window.addEventListener("scroll", handleScroll);

        // 컴포넌트 언마운트 시 리스너 제거
        return () => {
            window.removeEventListener("scroll", handleScroll);
        };
    }, []);

      // 드롭다운 외부 클릭 감지
    useEffect(() => {
        document.addEventListener("click", handleClickOutside);

        return () => {
        document.removeEventListener("click", handleClickOutside);
        };
    }, []);

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

    const handleClickOutside = (e: MouseEvent) => {
        if (!(e.target as HTMLElement).closest(".dropdown-container")) {
            setDropdownVisible(false);
        }
    };

    const buttonBaseClasses = "flex items-center gap-2 cursor-pointer remote-button";
    const getButtonClasses = (option: SortOption) => {
        return selectedSort === option
            ? `${buttonBaseClasses} border-b-2 border-black text-black`
            : `${buttonBaseClasses} text-gray-500 hover:text-black`;
    };

    const handleSignin = () => {
        keycloak?.login();
    };
    const toggleDropdown = () => {
        setDropdownVisible(!dropdownVisible);
        console.log(`드랍다운 상태 : ${dropdownVisible}`)
    }
    const handleLogout = () => {
        keycloak?.logout();
    };

    if (!keycloak) {
        // 로딩 중일 때 보여줄 스켈레톤 UI
        return (
            <div className={`fixed top-0 z-10 w-full flex justify-center shadow-md bg-green-500
                            transition-transform duration-300 
                            ${isVisible ? "translate-y-0" : "-translate-y-full"}`}>
                <div className="max-w-1728 2xl:max-w-1376 xl:max-w-1024 w-full h-16 flex items-center justify-between bg-yellow-400 ">
                    <div className="text-2xl font-bold cursor-pointer bg-red-400">
                        Miniblog
                    </div>
                    <div className="bg-white flex flex-row gap-4 items-center">
                        <div className="bg-gray-300 rounded-full h-12 w-12 animate-pulse" />
                        <div className="bg-gray-300 rounded-full h-12 w-12 animate-pulse" />
                    </div>
                </div>
            </div>
        );
    }

    return (
        <div className={`fixed top-0 z-10 w-full flex justify-center shadow-md bg-green-500
            transition-transform duration-300 
            ${isVisible ? "translate-y-0" : "-translate-y-full"}`}>
            <div className="max-w-1728 2xl:max-w-1376 xl:max-w-1024 w-full flex-col items-center justify-between  ">
                <div className="w-full h-16 flex items-center justify-between bg-yellow-400">
                    <div 
                        className="text-2xl md:text-xl font-bold cursor-pointer bg-red-400 "
                        onClick={toHome}
                    >
                        Miniblog
                    </div>
                    <div className="bg-white flex flex-row gap-2 items-center">
                        <div 
                            className="bg-white rounded-full cursor-pointer hover:bg-gray-200 p-2"
                        >
                            <IoMdSearch size={32}/>
                        </div>
                        {/* 로그인 상태 분기점 */}
                        {keycloak?.authenticated ? (
                            <div 
                                className="bg-white remote-button rounded-full cursor-pointer  flex justify-center items-center group relative dropdown-container"
                                onClick={toggleDropdown}
                            >
                                <FiUser size={32}/>
                                <div className=" text-xs text-gray-400 group-hover:text-black">▼</div>
                                {/* (로그인) 드랍다운 */}
                                {dropdownVisible && (
                                    <div className="absolute top-14 right-0 w-48 bg-gray-200 shadow-[0_6px_6px_-1px_rgba(0,0,0,0.1),_0_2px_4px_-1px_rgba(0,0,0,0.06)] z-10 rounded-md overflow-hidden">
                                        <div className="bg-gray-300 px-3 py-4 font-semibold hover:bg-gray-400 hover:text-blue-500">
                                            내 블로그
                                        </div>
                                        <div 
                                            className="bg-gray-300 px-3 py-4 font-semibold hover:bg-gray-400 hover:text-blue-500"
                                            onClick={toPostWrite}
                                        >
                                            게시글 작성
                                        </div>
                                        <div 
                                            className="bg-gray-300 px-3 py-4 font-semibold hover:bg-gray-400 hover:text-blue-500"
                                            onClick={() => {alert("히스토리로가자")}}
                                        >
                                            히스토리
                                        </div>
                                        <div 
                                            className="bg-gray-300 px-3 py-4 font-semibold hover:bg-gray-400 hover:text-blue-500"
                                            onClick={toSetting}
                                        >
                                            설정
                                        </div>
                                        <div 
                                            className="bg-gray-300 px-3 py-4 font-semibold hover:bg-gray-400 hover:text-blue-500"
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
                <div className="w-full h-12 flex font-bold gap-2 bg-blue-400 ">
                    <div className={getButtonClasses('views')} onClick={() => onSortChange('views')}>
                        <CiWavePulse1 className="text-2xl" />
                        조회수
                    </div>
                    <div className={getButtonClasses('latest')} onClick={() => onSortChange('latest')}>
                        <CiClock2 className="text-2xl" />
                        최신
                    </div>
                    <div className={getButtonClasses('likes')} onClick={() => onSortChange('likes')}>
                        <CiHeart className="text-2xl" />
                        좋아요
                    </div>
                </div>
            </div>
        </div>
    );
}

export default ExtendedHeader;