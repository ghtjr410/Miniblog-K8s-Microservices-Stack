import React, { useEffect, useRef, useState } from "react";
import Keycloak from "keycloak-js";
import SkeletonBlogHeader from "../../components/skeleton/SkeletonBlogHeader";
import BlogHeader from "../../components/header/blog/BlogHeader";
import { useParams } from "react-router-dom";
import { PiEyeLight } from "react-icons/pi";
import { FaAlignJustify, FaAngleLeft, FaHeart, FaRegComment } from "react-icons/fa";
import { TiArrowDownThick, TiArrowUpThick } from "react-icons/ti";
import { BsSearch } from "react-icons/bs";
import SkeletonBlogBody from "../../components/skeleton/SkeletonBlogBody";

interface Props {
    keycloak: Keycloak | null;
    keycloakStatus: "loading" | "authenticated" | "unauthenticated";
}

const TestBlogPage : React.FC<Props> = ({keycloak, keycloakStatus}) => {
    const { nickname } = useParams();
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


    if (!keycloak) {
        return (
            <>
                <SkeletonBlogHeader/>
                <SkeletonBlogBody/>
            </>
        )
    }

    return (
        <>
            <BlogHeader keycloak={keycloak} nickname={nickname!}/>
            {/* 전체 바디 (빈공간 포함) */}
            <div className="min-w-full min-h-screen flex justify-center bg-gray-100">
                {/* 본문 (빈공간 미포함) */}
                <div className="flex-1 flex justify-center bg-white shadow-lg max-w-1728 pt-20 pb-4 px-4 2xl:max-x-1376 xl:max-w-1024">
                    <div className="relative pt-32 pb-16 w-[768px] xs:w-full px-4 bg-white flex flex-col">
                        {/* 왼쪽 게시글 목록 */}
                        <aside className="absolute top-0 -left-72 w-64 min-h-72 flex h-full  xl:hidden   bg-white">
                            {/* Inner Box */}
                            <div className="mt-40 mb-10 py-4 w-full max-h-full flex flex-col  rounded-md">
                                <div // 블로그 이름
                                    className="px-2 py-4 text-xl bg-gray-100 rounded-md mb-2 text-center font-bold cursor-pointer"
                                    onClick={() => {alert("해당 블로그로 이동")}}
                                >
                                    가가가가가가가가가가
                                </div>
                                <div // 블로그 소개
                                    className="px-2 py-4 rounded-md border border-gray-300 text-sm bg-white  empty:hidden">
                                    일이삼사오육칠팔구십일이삼사오육칠팔구십일이삼사오육칠팔구십일이삼사오육칠팔구십일이삼사오육칠팔구십
                                </div>
                                <div className="p-2 pt-4">
                                    <div className="px-2 py-1 border border-gray-400 flex  justify-center items-center">
                                        <BsSearch size={20}/>
                                        <input 
                                            className="pl-2 w-full outline-none"
                                            type="text" />
                                    </div>
                                </div>
                                
                                <div // 블로그 이름
                                    className="py-4 px-2 text-base font-bold cursor-pointer hover:underline hover:text-gray-500"
                                >
                                    게시글 목록
                                </div>
                                <div // 글목록
                                    className="py-4 px-2 overflow-y-auto ">
                                    <ul className="space-y-4 border-l-2 border-gray-300 bg-gray-50">
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
                        {/* <div className="flex flex-col justify-center items-center">
                            <div className="w-[425px] 2xs:w-[325px] ">
                                <img
                                    className="ml-4" 
                                    src="https://images.ghtjr.com/e94915c9-e28d-4217-ad2e-e30a7a685d7c_empty_box_miniblog.png" alt="" />
                            </div>
                            <div className="mt-6 text-2xl 2xs:text-xl">
                                아직 게시글이 존재하지 않습니다
                            </div>
                        </div> */}
                    
                        {/* 본문 그리드 */}
                        <div className="grid grid-cols-2 gap-6 mx-auto xs:flex xs:flex-col" >

                            {Array.from({ length: 0 }).map((_, index) => (
                                <div
                                    key={index}
                                    className="bg-white w-full aspect-[17/20] md:aspect-[20/19] xs:aspect-[20/16] flex flex-col rounded-lg shadow-lg overflow-hidden hover:shadow-xl transform hover:-translate-y-2 transition duration-300 ease-in-out">
                                    <div className="h-[167px] overflow-hidden cursor-pointer md:h-[255px] xs:h-[372px]">
                                        <img
                                            src={"https://velog.velcdn.com/images/sehyunny/post/fd9273aa-7a11-4c9b-ba3d-450dd04e6a1c/image.png"}
                                            alt="content"
                                            className="h-full w-full object-cover"
                                        />
                                    </div>
                                    <div className="flex-1 flex-col p-4">
                                        <div className=" text-lg mb-1 font-black line-clamp-1">
                                            지난 몇 달간, 저는 AI에 대해 점점 더 불안해하는 많은 주니어 개발자들과 얘기를 나눴습니다. 그들은 GPT-4와 같은 도구들의 갈수록 발전해가는 인상적인 데모를 보고
                                        </div>
                                        <div className="text-sm text-gray-600 line-clamp-3">
                                            이거 이미지없으면 6줄로하자 대해 점점 더 불안해하는 많은 주니어 개발자들과 얘기를 나눴습니다. 그들은 GPT-4와 같은 도구들의 갈수록 발전해가는 인상적인 데모를 보고
                                            지난 몇 달간, 저는 AI에 대해 점점 더 불안해하는 많은 주니어 개발자들과 얘기를 나눴습니다. 그들은 GPT-4와 같은 도구들의 갈수록 발전해가는 인상적인 데모를 보고
                                        </div>
                                    </div>
                                    <div className=" flex flex-col justify-end text-gray-500">
                                        <div className="px-4 py-2.5 text-xs">
                                            2020년 12월 21일
                                        </div>
                                        <div className="px-4 py-2.5 flex justify-between text-xs border-t border-gray-200 text-black ">
                                            <b className="text-black">최호석입니다</b>
                                            <div className="flex gap-2">
                                                <div className="flex items-center gap-1">
                                                    <PiEyeLight size={17}/> 
                                                    <div>40</div>
                                                </div>
                                                <div className="flex items-center gap-1">
                                                    <FaHeart size={14}/>
                                                    <div>40</div>
                                                </div>
                                                <div className="flex items-center gap-1">
                                                    <FaRegComment size={15} />
                                                    <div>20</div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            ))}
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
                    {/* <div className="p-2 bg-white border-b border-gray-200">
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

            </div>
        </>
    )
}

export default TestBlogPage;