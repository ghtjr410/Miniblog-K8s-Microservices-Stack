import { useEffect, useState } from "react";
import useNavigationHelper from "../../util/navigationUtil";

const SkeletonCommonHeader = () => {
    const [isVisible, setIsVisible] = useState<boolean>(true);
    const { toHome } = useNavigationHelper();

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

    return (
        <div className={`fixed top-0 z-10 w-full flex justify-center shadow-md bg-white
                        transition-transform duration-300 
                        ${isVisible ? "translate-y-0" : "-translate-y-full"}`}>
            <div className="max-w-1728 2xl:max-w-1376 xl:max-w-1024 md:mx-4 w-full flex flex-col items-center justify-center">
                <div className="w-full h-16 flex items-center justify-between ">
                    <div className="text-2xl md:text-xl font-bold cursor-pointer " onClick={toHome}>
                        <div className="xs:hidden" >Miniblog</div>
                        <div className="h-12 w-12 hidden xs:flex overflow-hidden">
                            <img 
                            className="h-full w-full object-cover"
                            src="/miniblog_48x48.png" alt="" />
                        </div>
                    </div>
                    <div className="flex flex-row gap-4 items-center">
                        <div className="bg-gray-300 rounded-full h-12 w-12 animate-pulse" />
                        <div className="bg-gray-300 rounded-full h-12 w-12 animate-pulse" />
                    </div>
                </div>
            </div>
        </div>
    );
}

export default SkeletonCommonHeader;