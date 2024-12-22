import React, { useEffect, useState } from "react"; 
import Keycloak from "keycloak-js";
import SkeletonCommonHeader from "../../components/skeleton/SkeletonCommonHeader";
import SkeletonSettingBody from "../../components/skeleton/SkeletonSettingBody";
import Header from "../../components/header/common/Header";
import { IoWarningOutline } from "react-icons/io5";

interface Props {
    keycloak: Keycloak | null;
    keycloakStatus: "loading" | "authenticated" | "unauthenticated";
}

const TestSettingPage: React.FC<Props> = ({ keycloak, keycloakStatus }) => {

    // if (!keycloak) {
    //     return (
    //         <>
    //             <SkeletonCommonHeader/>
    //             <SkeletonSettingBody/>
    //         </>
    //     )
    // }
    return (
        <>
            <Header keycloak={keycloak}/>
            {/* 전체 바디 (빈공간 포함) */}
            <div className="min-w-full min-h-screen flex justify-center  bg-gray-100">
                {/* 본문바디 (빈공간 미포함) */}
                <div className="max-w-1728 2xl:max-w-1376 xl:max-w-1024  flex-1 flex justify-center bg-white shadow-lg">
                    <div className="relative pt-32 pb-16 w-[768px] xs:w-full xs:px-4  2xs:px-2 bg-white flex flex-col">
                        <div className="flex border-b border-gray-200 py-4 xs:flex-col xs:gap-2">
                            <div className="text-xl font-bold w-40 flex items-center">
                                아이디
                            </div>
                            <input type="text" value={"최호석"} readOnly className="border border-gray-300 text-gray-500 rounded px-3 py-2 bg-gray-100 xs:w-[200px]"/>
                        </div>
                        <div className="flex border-b border-gray-200 py-4 xs:flex-col xs:gap-2">
                            <div className="text-xl font-bold w-40 flex items-center">
                                닉네임
                            </div>
                            <input type="text" value={"첫번째닉네임"} readOnly className="border border-gray-300 text-gray-500 rounded px-3 py-2 bg-gray-100 xs:w-[200px]"/>
                        </div>
                        <div className="flex border-b border-gray-200 py-4 xs:flex-col xs:gap-2 ">
                            <div className="text-xl font-bold w-40 flex items-center">
                                이메일
                            </div>
                            <input type="text" value={"ghtjr410@gmail.com"} readOnly className="border border-gray-300 text-gray-500 w-96 rounded px-3 py-2 bg-gray-100 xs:w-[300px]"/>
                        </div>
                        <div className="flex border-b border-gray-200 py-4 xs:flex-col xs:gap-2">
                            <div className="text-xl font-bold w-40 flex items-center">
                                블로그 제목
                            </div>
                            <div className="flex items-center gap-2 xs:flex xs:items-start xs:flex-col xs:gap-2">
                                <input 
                                    type="text" 
                                    value={"블로그제목입니다."} 
                                    maxLength={10}
                                    // onChange={(e) => setEditTitle(e.target.value)} 
                                    // readOnly={!isEditingTitle} 
                                    className={`border border-gray-300 rounded px-3 py-2 w-96 bg-white xs:w-full`}
                                />
                                <button className="px-6 py-1.5 text-lg rounded border border-black bg-white font-black hover:bg-black hover:text-white xs:ml-auto transition-colors duration-300 ease-in-out"
                                        >
                                        수정
                                </button>
                                {/* <div className="xs:ml-auto">
                                        <button 
                                            className="px-6 py-1.5 text-lg rounded bg-green-400  font-bold text-white hover:bg-green-500"
                                        >
                                            저장
                                        </button>
                                        <button 
                                            className="ml-2 px-6 py-1.5 text-lg rounded bg-red-400 font-bold text-white hover:bg-red-600"
                                        >
                                            취소
                                        </button>
                                </div> */}
                            </div>
                        </div>
                        <div className="flex border-b border-gray-200 py-4 xs:flex-col xs:gap-2">
                            <div className="text-xl font-bold w-40 flex items-center">
                                블로그 소개
                            </div>
                            <div className="flex items-center gap-2 xs:flex xs:items-start xs:flex-col xs:gap-2">
                                <input 
                                    type="text" 
                                    value={"블로그 소개입니다."} 
                                    maxLength={50}
                                    // onChange={(e) => setEditTitle(e.target.value)} 
                                    // readOnly={!isEditingTitle} 
                                    className={`border border-gray-300 rounded px-3 py-2 w-96 bg-white xs:w-full`}
                                />
                                {/* <button className="px-6 py-1.5 text-lg rounded border border-black bg-white font-black hover:bg-black hover:text-white xs:ml-auto transition-colors duration-300 ease-in-out"
                                        >
                                        수정
                                </button> */}
                                <div className="xs:ml-auto">
                                        <button 
                                            className="px-6 py-1.5 text-lg rounded bg-green-400  font-bold text-white hover:bg-green-500"
                                        >
                                            저장
                                        </button>
                                        <button 
                                            className="ml-2 px-6 py-1.5 text-lg rounded bg-red-400 font-bold text-white hover:bg-red-600"
                                        >
                                            취소
                                        </button>
                                </div>
                            </div>
                        </div>
                        <div className="flex border-b border-gray-200 py-4 xs:flex-col xs:gap-2">
                            <div className="text-xl font-bold w-40 flex items-center">
                                회원탈퇴
                            </div>
                            <button 
                                className="bg-red-400 px-6 py-1.5 text-lg rounded text-white font-semibold hover:bg-red-700 flex items-center justify-center gap-2"
                            >
                                <IoWarningOutline size={25}/>
                                회원탈퇴
                                <div>　</div>
                            </button>
                        </div>







                    </div>
                </div>
            </div>
        </>
    )
}

export default TestSettingPage;