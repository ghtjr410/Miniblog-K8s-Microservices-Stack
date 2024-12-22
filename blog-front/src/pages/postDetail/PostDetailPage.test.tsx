import React, { useEffect, useState } from "react"; 
import Keycloak from "keycloak-js";
import SkeletonCommonHeader from "../../components/skeleton/SkeletonCommonHeader";
import SkeletonPostDetailBody from "../../components/skeleton/SkeletonPostDetailBody";
import Header from "../../components/header/common/Header";
import { BsSearch } from "react-icons/bs";
import { FaAlignJustify, FaAngleLeft, FaHeart } from "react-icons/fa";
import { TiArrowDownThick, TiArrowUpThick } from "react-icons/ti";

interface Props {
    keycloak: Keycloak | null;
    keycloakStatus: "loading" | "authenticated" | "unauthenticated";
}

const TestPostDetailPage: React.FC<Props> = ({ keycloak, keycloakStatus }) => {
    const [showRemote, setShowRemote] = useState<boolean>(true);
    const [showSlide, setShowSlide] = useState<boolean>(false);

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

    // 화면 크기가 1440px 이하일 때 showSlide를 false로 설정
    useEffect(() => {
        const handleResize = () => {
            if (window.innerWidth <= 1440) {
                setShowSlide(false);
            }
        };

        // 초기 실행
        handleResize();

        window.addEventListener("resize", handleResize);

        return () => {
            window.removeEventListener("resize", handleResize);
        };
    }, []);

    const toggleSlide = () => {
        setShowSlide((prev) => !prev);
    };
    // if (!keycloak) {
    //     return (
    //         <>
    //             <SkeletonCommonHeader/>
    //             <SkeletonPostDetailBody/>
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
                    {/* 게시글 섹션 */}
                    <div className="relative pt-32 pb-16 w-[768px] xs:w-full xs:px-4  2xs:px-2 bg-gray-500 flex flex-col cursor-text">
                        {/* 왼쪽 게시글 목록 */}
                        <aside className="absolute top-0 -left-72 w-64 min-h-72 flex h-full  xl:hidden   bg-white">
                            {/* Inner Box */}
                            <div className="mt-40 mb-10 py-4 w-full max-h-full flex flex-col shadow-header-dropdown-shadow rounded-md">
                                <div // 블로그 이름
                                    className="px-2 py-4 text-xl text-center font-bold cursor-pointer"
                                    onClick={() => {alert("해당 블로그로 이동")}}
                                >
                                    가가가가가가가가가가
                                </div>
                                <div // 블로그 소개
                                    className="px-2 py-4 rounded-md border border-gray-300 text-sm bg-white  empty:hidden">
                                    일이삼사오육칠팔구십일이삼사오육칠팔구십일이삼사오육칠팔구십일이삼사오육칠팔구십일이삼사오육칠팔구십
                                </div>
                                {/* <div className="p-2 pt-4">
                                    <div className="px-2 py-1 border border-gray-400 flex  justify-center items-center">
                                        <BsSearch size={20}/>
                                        <input 
                                            className="pl-2 w-full outline-none"
                                            type="text" />
                                    </div>
                                </div> */}
                                
                                <div // 블로그 이름
                                    className="px-4 pt-4 text-base font-bold cursor-pointer hover:underline hover:text-gray-500"
                                    onClick={() => {alert("해당 블로그로 이동")}}
                                >
                                    전체보기 (1)
                                </div>
                                <div // 글목록
                                    className="py-4 px-2 overflow-y-auto ">
                                    <ul className="space-y-4 border-l-2 border-gray-300">
                                        {Array.from({ length: 50 }).map((_, index) => (
                                            <li 
                                            key={index}
                                            className=" hover-text-blink px-2 text-sm truncate cursor-pointer"
                                            onClick={() => {alert("해당 게시글로 이동")}}
                                        >
                                            메뉴 항목 1@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
                                        </li>
                                        ))}
                                    </ul>
                                </div>
                            </div>
                        </aside>
                        <div className="bg-gray-100">
                            <div className="text-5xl font-bold bg-slate-100 leading-snug">
                                (번역) 자바스크립트의 Scheduler API 활용하기
                            </div>
                        </div>
                        {/* 게시글 정보 */}
                            <div className="flex mt-8 justify-between">
                                <div className="flex text-base gap-2">
                                    <div className="font-bold">
                                        첫번째닉네임
                                    </div>
                                    <div>·</div>
                                    <div>
                                        2020년 12월 30일
                                    </div>
                                </div>
                            </div>
                    </div>
                </div>
            </div>

            {/* 리모콘 */}
            {showRemote && (
                <aside className="fixed flex flex-col gap-4 top-[calc(50%-96px)] right-[calc(50%-468px)] sm:right-2">
                    <button // 게시글 목록 슬라이드
                        className=" w-10 h-10 bg-gray-700 hover:bg-gray-500 text-white rounded-md hidden xl:flex items-center justify-center"
                        onClick={toggleSlide}
                    >
                        <FaAlignJustify size={25} />
                    </button>
                    <button // 게시글 목록 슬라이드
                        className="remote-button w-10 h-10 bg-gray-700 hover:bg-gray-500 text-white rounded-md flex items-center justify-center"
                        onClick={toggleSlide}
                    >
                        <FaHeart size={25} />
                    </button>
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

            {/* 슬라이드 메뉴 */}
            <div
                className={`remote-button hidden xl:flex flex-col fixed top-0 left-0 w-64 h-screen bg-gray-50 shadow-md text-black z-20 transform ${
                showSlide ? " translate-x-0" : "-translate-x-full"
                } transition-transform duration-300`}
            >
                <div className="h-16 p-4  flex justify-between items-center bg-white">
                    <div 
                        className="text-lg text-center font-bold cursor-pointer"
                        onClick={() => {alert("해당 블로그로 이동")}}
                    >
                        가가가가가가가가가가
                    </div>
                    <button 
                        className="text-gray-400 hover:text-black"
                        onClick={toggleSlide}>
                        <FaAngleLeft size={20} />
                    </button>
                </div>
                <div className="p-2 bg-white border-b border-gray-200">
                    <div className="px-2 py-1 border border-gray-400 flex  justify-center items-center">
                        <BsSearch size={20}/>
                        <input 
                            className="pl-2 w-full outline-none"
                            type="text" />
                    </div>
                </div>
                <div // 블로그 이름
                    className="px-4 pt-4 text-base font-bold cursor-pointer hover:underline hover:text-gray-500"
                    onClick={() => {alert("해당 블로그로 이동")}}
                >
                    전체보기 (1)
                </div>
                <ul className="px-4 py-8 space-y-4  overflow-y-auto">
                    {Array.from({ length: 50 }).map((_, index) => (
                        <li 
                            className="hover-text-blink px-2 text-sm truncate cursor-pointer"
                            onClick={() => {alert("해당 게시글로 이동")}}
                        >
                            메뉴 항목 1@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
                        </li>
                    ))}
                </ul>
            </div>

            {/* 슬라이드 오버레이 */}
            {showSlide && (
                <div
                    className="fixed top-0 hidden xl:flex left-0 w-full h-full bg-black opacity-50 z-10"
                    onClick={toggleSlide}>
                </div>
            )}
        </>
    )
}

export default TestPostDetailPage;