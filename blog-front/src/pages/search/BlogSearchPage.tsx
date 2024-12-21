import React, { useEffect, useRef, useState } from "react";
import Keycloak from "keycloak-js";
import Header from "../../components/header/common/Header";
import { useParams, useSearchParams } from "react-router-dom";
import { BsSearch } from "react-icons/bs";
import { FaAlignJustify, FaAngleLeft, FaHeart, FaRegComment } from "react-icons/fa";
import { PiEyeLight } from "react-icons/pi";
import { TiArrowDownThick, TiArrowUpThick } from "react-icons/ti";
import SkeletonCommonHeader from "../../components/skeleton/SkeletonCommonHeader";
import SkeletonSearchBody from "../../components/skeleton/SkeletonSearchBody";

interface Props {
    keycloak: Keycloak | null;
    keycloakStatus: "loading" | "authenticated" | "unauthenticated";
}

const BlogSearchPage : React.FC<Props> = ({keycloak, keycloakStatus}) => {
    const { nickname } = useParams();
    const [searchParams, setSearchParams] = useSearchParams();
    const keyword = searchParams.get('keyword');
    const [inputValue, setInputValue] = useState(keyword || "");
    const inputRef = useRef<HTMLInputElement>(null);
    const [showRemote, setShowRemote] = useState<boolean>(true);
    const [showSlide, setShowSlide] = useState<boolean>(false);


    useEffect(() => {
        console.log(`Nickname : ${nickname}, Keyword: ${keyword}`);
    }, [nickname, keyword]);

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

    const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
        if (e.key === 'Enter') {
            // 검색어가 존재하면 쿼리 파라미터 추가
            if (inputValue.trim() !== "") {
                setSearchParams({ keyword: inputValue.trim() });
            } else {
                // 검색어가 비어있으면 쿼리 파라미터를 제거하고 기본 주소로
                setSearchParams({});
            }
        }
    };
    const toggleSlide = () => {
        setShowSlide((prev) => !prev);
    };

    if (!keycloak) {
        return (
            <>
                <SkeletonCommonHeader/>
                <SkeletonSearchBody/>
            </> 
        )
    }

    return (
        <>
            <Header keycloak={keycloak} />
            {/* 전체 바디(빈공간포함) */}
            <div className="min-w-full min-h-screen flex justify-center  bg-gray-100">
                {/* 본문(빈공간 미포함) */}
                <div className="max-w-1728 2xl:max-w-1376 xl:max-w-1024  flex-1 flex justify-center bg-white shadow-lg">

                    {/* 검색 섹션 */}
                    <div className="relative pt-32 pb-16 w-[768px] xs:w-full xs:px-4 bg-white flex flex-col cursor-text">
                        {/* 왼쪽 게시글 목록 */}
                        <aside className="min-h-72 mb-14 flex flex-col absolute max-h-[90%] top-40 -left-72 w-64 xl:hidden shadow-lg  bg-white">
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
                                <ul className=" space-y-4 border-l-2 border-gray-300">
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
                        </aside>

                        {/* 검색 기준유저 명 */}
                        <div className="mt-4 mb-2 font-semibold">
                            {`'${nickname}' 님이 작성한 게시글 검색`}
                        </div>
                        {/* 검색 인풋 */}
                        <div 
                            className="px-6 xs:px-4 py-1 h-16 xs:h-10 mb-6 flex items-center border border-black w-full bg-white focus-within:border-gray-400"
                            onClick={() => inputRef.current?.focus()}>
                            <BsSearch className="mr-5 text-3xl xs:text-lg xs:mr-3"/>
                            <input
                                ref={inputRef} 
                                className="h-8 text-2xl xs:text-lg w-full outline-none"
                                placeholder="검색어를 입력하세요"
                                type="text"
                                value={inputValue}
                                onChange={(e) => setInputValue(e.target.value)}
                                onKeyDown={handleKeyDown} />
                        </div>
                        <div className="text-lg xs:text-base mb-16">
                            총 <b>27개</b>의 게시글을 찾았습니다
                        </div>
                        {/* <div className="flex flex-col justify-center items-center">
                            <div className="w-[425px] 2xs:w-[325px] ">
                                <img
                                    className="ml-4" 
                                    src="https://images.ghtjr.com/e94915c9-e28d-4217-ad2e-e30a7a685d7c_empty_box_miniblog.png" alt="" />
                            </div>
                            <div className="mt-6 text-2xl 2xs:text-xl">
                                검색 결과가 없습니다.
                            </div>
                        </div> */}
                        <div className="flex flex-col gap-16">
                            {Array.from({ length: 2 }).map((_, index) => (
                                <div 
                                    key={index}
                                    className=" flex flex-col w-full h-auto overflow-hidden rounded-lg shadow-lg hover:shadow-xl transform hover:-translate-y-2 transition duration-300 ease-in-out ">
                                    <div className=" max-h-[400px] overflow-hidden cursor-pointer bg-red-300">
                                        <img
                                            src={"https://velog.velcdn.com/images/sehyunny/post/fd9273aa-7a11-4c9b-ba3d-450dd04e6a1c/image.png"}
                                            alt="content"
                                            className="h-full w-full object-cover"
                                        />
                                    </div>
                                    <div className="px-2 my-6 xs:my-2  text-3xl xs:text-lg font-black line-clamp-2">
                                        지난 몇 달간, 저는 AI에 대해 점점 더 불안해하는 많은 주니어 개발자들과 얘기를 나눴습니다. 그들은 GPT-4와 같은 도구들의 갈수록 발전해가는 인상적인 데모를 보고
                                    </div>
                                    <p className="px-2 xs:text-sm line-clamp-3 xs:line-clamp-4 mb-10">지난 몇 달간, 저는 AI에 대해 점점 더 불안해하는 많은 주니어 개발자들과 얘기를 나눴습니다. 그들은 GPT-4와 같은 도구들의 갈수록 발전해가는 인상적인 데모를 보고있습니다. 지난 몇 달간, 저는 AI에 대해 점점 더 불안해하는 많은 주니어 개발자들과 얘기를 나눴습니다. 그들은 GPT-4와 같은 도구들의 갈수록 발전해가는 인상적인 데모를 보고있습니다. 지난 몇 달간, 저는 AI에 대해 점점 더 불안해하는 많은 주니어 개발자들과 얘기를 나눴습니다. 그들은 GPT-4와 같은 도구들의 갈수록 발전해가는 인상적인 데모를 보고있습니다.</p>
                                    <div className=" w-full flex items-center px-4 py-2 border-t border-gray-200 text-gray-500 text-sm
                                            2xs:h-auto 2xs:text-xs">
                                        <div className="flex bg-white  gap-2">
                                            <div>
                                                2024년 12월 19일
                                            </div>
                                            <div className="flex items-center gap-1">
                                                <FaRegComment size={15} />
                                                <div>20</div>
                                            </div>
                                            <div className="flex items-center gap-1">
                                                <PiEyeLight size={17}/> 
                                                <div>40</div>
                                            </div>
                                            <div className="flex items-center gap-1">
                                                <FaHeart size={14}/>
                                                <div>40</div>
                                            </div>
                                        </div>
                                    </div>

                                </div>
                            ))}

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
        </>
    )
}

export default BlogSearchPage;