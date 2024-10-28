import React from "react";
import { CiBellOn } from "react-icons/ci";
import { CiSearch } from "react-icons/ci";
import { CiUser } from "react-icons/ci";
import { Outlet } from "react-router-dom";


const Header = () => {
    return (
    <div>
        {/* Header 영역 */}
        <div className="bg-slate-100 shadow-md flex justify-center">
            <div className="bg-slate-200 h-16 max-w-screen-2xl w-full flex items-center justify-between px-4">
                <div className="bg-slate-300 text-2xl font-bold cursor-pointer">
                    MiniBlog
                </div>
                <div className="bg-slate-300 flex flex-row gap-4 items-center">
                    <div className="bg-slate-400 text-3xl cursor-pointer rounded-full p-1">
                        <CiBellOn />
                    </div>
                    <div className="bg-slate-400 text-3xl cursor-pointer rounded-full p-1">
                        <CiSearch />
                    </div>
                    <div className="bg-slate-400 flex items-center cursor-pointer group">
                        <CiUser className="text-4xl" />
                        <div className="text-xs text-gray-500 group-hover:text-black">▼</div>
                    </div>
                </div>
            </div>
        </div>
        {/* Children 영역 */}
        <div className="pt-6"><Outlet /></div>
    </div>
    );
}

export default Header;