import React, { useEffect} from "react"; 
import Keycloak from "keycloak-js";
import HistoryHeader from "../../components/header/history/HistroyHeader";
import { useLocation } from "react-router-dom";
import { FaHeart, FaRegComment } from "react-icons/fa";
import { PiEyeLight } from "react-icons/pi";
import SkeletonHistoryHeader from "../../components/skeleton/SkeletonHistoryHeader";
import SkeletonCardBody from "../../components/skeleton/SkeletonCardBody";

interface Props {
    keycloak: Keycloak | null;
    keycloakStatus: "loading" | "authenticated" | "unauthenticated";
}

const HistoryPage : React.FC<Props> = ({keycloak, keycloakStatus}) => {
    const location = useLocation();

    useEffect(() => {
        if (location.pathname === "/history/comments") {
            console.log("Comments!");
        } else {
            console.log("Liked!")
        }
    }, [location.pathname]);

    if (!keycloak) {
        return (
            <>
                <SkeletonHistoryHeader/>
                <SkeletonCardBody/>
            </>
        )
    }

    return (
        <>
            <HistoryHeader keycloak={keycloak}/>
            {/* 전체 바디 (빈공간 포함) */}
            <div className="min-w-full min-h-screen flex justify-center bg-gray-100">
                {/* 본문 (빈공간 미포함) */}
                <div className="flex-1 max-w-1728 pt-40 pb-4 px-4 2xl:max-w-1376 xl:max-w-1024 ">
                    {/* 본문 그리드 */}
                    <div className=" grid grid-cols-5 gap-6 mx-auto
                        2xl:max-w-1376 2xl:grid-cols-4
                        xl:max-w-1024 xl:grid-cols-3 
                        md:grid-cols-2 
                        xs:flex xs:flex-col">
                        {Array.from({ length: 40 }).map((_, index) => (
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
                    {/* 빈 리스트 */}
                    {/* <div className="flex flex-col justify-center items-center">
                        <div className="w-[425px] 2xs:w-[325px] ">
                            <img
                                className="ml-4" 
                                src="https://images.ghtjr.com/e94915c9-e28d-4217-ad2e-e30a7a685d7c_empty_box_miniblog.png" alt="" />
                        </div>
                        <div className="mt-6 text-2xl 2xs:text-xl text-gray-500">
                            히스토리가 비어있습니다.
                        </div>
                    </div> */}
                </div>
            </div>
        </>
    );
}

export default HistoryPage;