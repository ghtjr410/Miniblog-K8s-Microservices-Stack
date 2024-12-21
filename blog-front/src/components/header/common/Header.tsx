import React, { useEffect, useState } from "react";
import Keycloak from 'keycloak-js';
import { IoMdSearch } from "react-icons/io";
import { FiUser } from "react-icons/fi";
import useNavigationHelper from "../../../util/navigationUtil";

interface Props{
    keycloak: Keycloak | null;
}

const Header: React.FC<Props> = ({keycloak}) => {
    const [dropdownVisible, setDropdownVisible] = useState<boolean>(false);
    const [isVisible, setIsVisible] = useState<boolean>(true);
    const { toHome, toSearch, toHistory, toPostWrite, toSetting} = useNavigationHelper();

    let lastScrollY = window.scrollY;

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

    return (
        <div className={`fixed top-0 z-10 w-full flex justify-center shadow-md bg-white
                        transition-transform duration-300 
                        ${isVisible ? "translate-y-0" : "-translate-y-full"}`}>
            <div className="max-w-1728 2xl:max-w-1376 xl:max-w-1024 md:mx-4 w-full flex flex-col items-center justify-center">
                <div className="w-full h-16 flex items-center justify-between">
                    <div className="text-2xl md:text-xl font-bold cursor-pointer " onClick={toHome}>
                        <div className="xs:hidden" >Miniblog</div>
                        <div className="h-12 w-12 hidden xs:flex overflow-hidden">
                            <img className="h-full w-full object-cover" src="/miniblog_48x48.png" alt="" />
                        </div>
                    </div>
                    <div className="bg-white flex flex-row gap-2 items-center">
                        <div 
                            className="bg-white rounded-full cursor-pointer hover:bg-gray-100 p-2"
                            onClick={toSearch}
                        >
                            <IoMdSearch size={32}/>
                        </div>
                        {/* 로그인 상태 분기점 */}
                        {keycloak?.authenticated ? (
                            <div 
                                className="bg-white rounded-full cursor-pointer  flex justify-center items-center group relative dropdown-container"
                                onClick={toggleDropdown}
                            >
                                <FiUser size={32}/>
                                <div className="text-xs text-gray-400 group-hover:text-black">▼</div>
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
                
            </div>
        </div>
    );
}

export default Header;