import React, { useState } from "react";
import Keycloak from 'keycloak-js';
import { CiBellOn } from "react-icons/ci";
import { CiSearch } from "react-icons/ci";
import { CiUser } from "react-icons/ci";
import { Outlet } from "react-router-dom";
import { useNavigation } from "../../util/navigation";
import { ROUTES } from "../../constants/routes";

interface Props{
    keycloak: Keycloak | null;
}

const Header: React.FC<Props> = ({keycloak}) => {
    const [dropdownVisible, setDropdownVisible] = useState<boolean>(false);

    const { navigateTo } = useNavigation();


    const handleKeycloak = () => {
        console.log(keycloak);
    };
    const handleToken = () => { 
        console.log(keycloak?.token);
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
            <div className="bg-slate-100 shadow-md flex justify-center">
                <div className="bg-slate-200 h-16 max-w-screen-2xl w-full flex items-center justify-between px-4">
                    <div className="bg-slate-300 text-2xl font-bold">
                        MiniBlog
                    </div>
                    <div className="bg-slate-300 flex flex-row gap-4 items-center">
                        <div className="bg-slate-400 rounded-full h-10 w-10 animate-pulse" />
                        <div className="bg-slate-400 rounded-full h-10 w-10 animate-pulse" />
                        <div className="bg-slate-400 rounded-full h-10 w-10 animate-pulse" />
                    </div>
                </div>
            </div>
        );
    }

    return (
    <div className="flex flex-col">
        {/* Header 영역 */}
        <div className="bg-slate-100 shadow-md flex justify-center">
            <div className="bg-slate-200 h-16 max-w-screen-2xl w-full flex items-center justify-between">
                <div className="bg-slate-300 text-2xl font-bold cursor-pointer">
                    MiniBlog
                </div>
                <div className="bg-slate-300 flex flex-row gap-4 items-center">
                    <div 
                        className="bg-slate-400 text-3xl cursor-pointer rounded-full p-1"
                        onClick={handleKeycloak}
                    >
                        <CiBellOn />
                    </div>
                    <div 
                        className="bg-slate-400 text-3xl cursor-pointer rounded-full p-1"
                        onClick={handleToken}
                    >
                        <CiSearch />
                    </div>
                    {keycloak?.authenticated ? (
                            <div 
                                className="bg-slate-400 flex items-center cursor-pointer group relative"
                                onClick={toggleDropdown}
                            >
                                <CiUser className="text-4xl" />
                                <div className="text-xs text-gray-500 group-hover:text-black">▼</div>
                                {dropdownVisible && (
                                    <div className="absolute top-14 right-0 w-48 bg-slate-200 shadow-[0_6px_6px_-1px_rgba(0,0,0,0.1),_0_2px_4px_-1px_rgba(0,0,0,0.06)]">
                                        <div className="bg-slate-300 px-3 py-4 font-semibold hover:bg-slate-400 hover:text-blue-500">
                                            내 블로그
                                        </div>
                                        <div 
                                            className="bg-slate-300 px-3 py-4 font-semibold hover:bg-slate-400 hover:text-blue-500"
                                            onClick={() => navigateTo(ROUTES.POST_EDIT)}
                                        >
                                            게시글 작성
                                        </div>
                                        <div className="bg-slate-300 px-3 py-4 font-semibold hover:bg-slate-400 hover:text-blue-500">
                                            읽기 목록
                                        </div>
                                        <div className="bg-slate-300 px-3 py-4 font-semibold hover:bg-slate-400 hover:text-blue-500">
                                            설정
                                        </div>
                                        <div 
                                            className="bg-slate-300 px-3 py-4 font-semibold hover:bg-slate-400 hover:text-blue-500"
                                            onClick={handleLogout}
                                        >
                                            로그아웃
                                        </div>
                                    </div>
                                )}
                            </div>
                        ) : (
                            <div
                                className="bg-black font-bold text-white text-lg rounded-2xl px-4 py-1 cursor-pointer"
                                onClick={handleSignin}
                            >
                                로그인
                            </div>
                        )}
                </div>
            </div>
        </div>
        {/* Outlet 영역 */}
        <div className="pt-6 flex-1  bg-slate-600">
            <Outlet />
        </div>
    </div>
    );
}

export default Header;