const SkeletonSettingBody = () => {
    return (
        <div className="min-w-full min-h-screen flex justify-center  bg-gray-100">
            {/* 본문(빈공간 미포함) */}
            <div className="max-w-1728 2xl:max-w-1376 xl:max-w-1024  flex-1 flex justify-center bg-white shadow-lg">
                {/* 검색 섹션 */}
                <div className="relative pt-32 pb-16 w-[768px] xs:w-full xs:px-4  2xs:px-2 bg-white flex flex-col">
                <div className="flex border-b border-gray-200 py-4 xs:flex-col xs:gap-2">
                            <div className="text-xl font-bold w-40 flex items-center ">
                                <div className="rounded-full bg-gray-300 animate-pulse">
                                    　　　
                                </div>
                            </div>
                            <div className="rounded-full bg-gray-300 animate-pulse px-3 py-2 xs:w-[200px]"> 
                                　　　　　　　　　　　
                            </div>
                        </div>
                        <div className="flex border-b border-gray-200 py-4 xs:flex-col xs:gap-2">
                            <div className="text-xl font-bold w-40 flex items-center">
                                <div className="rounded-full bg-gray-300 animate-pulse">
                                    　　　
                                </div>
                            </div>
                            <div className="rounded-full bg-gray-300 animate-pulse px-3 py-2 xs:w-[200px]"> 
                                　　　　　　　　　　　
                            </div>
                        </div>
                        <div className="flex border-b border-gray-200 py-4 xs:flex-col xs:gap-2">
                            <div className="text-xl font-bold w-40 flex items-center">
                                <div className="rounded-full bg-gray-300 animate-pulse">
                                    　　　
                                </div>

                            </div>
                            <div className="rounded-full bg-gray-300 animate-pulse px-3 py-2 xs:w-[200px]"> 
                                　　　　　　　　　　　
                            </div>
                        </div>
                        <div className="flex border-b border-gray-200 py-4 xs:flex-col xs:gap-2">
                            <div className="text-xl font-bold w-40 flex items-center">
                                <div className="rounded-full bg-gray-300 animate-pulse">
                                    　　　　　
                                </div>
                            </div>
                            <div className="flex items-center gap-2 xs:flex xs:items-start xs:flex-col xs:gap-2">
                                <div className="rounded-full bg-gray-300 animate-pulse px-3 py-2 xs:w-[200px]"> 
                                    　　　　　　　　　　　　　　　　　　　　 　　
                                </div>
                                <button className="rounded-full bg-gray-300 animate-pulse px-6 py-2 font-bold  xs:ml-auto"
                                        >
                                        　　
                                </button>
                            </div>
                        </div>
                        <div className="flex border-b border-gray-200 py-4 xs:flex-col xs:gap-2">
                            <div className="text-xl font-bold w-40 flex items-center">
                                <div className="rounded-full bg-gray-300 animate-pulse">
                                    　　　　　
                                </div>
                            </div>
                            <div className="flex items-center gap-2 xs:flex xs:items-start xs:flex-col xs:gap-2">
                                <div className="rounded-full bg-gray-300 animate-pulse px-3 py-2 xs:w-[200px]"> 
                                    　　　　　　　　　　　　　　　　　　　　 　　
                                </div>
                                <button className="rounded-full bg-gray-300 animate-pulse px-6 py-2 font-bold  xs:ml-auto"
                                        >
                                        　　
                                </button>
                            </div>
                        </div>
                        <div className="flex border-b border-gray-200 py-4 xs:flex-col xs:gap-2">
                            <div className="text-xl font-bold w-40 flex items-center">
                                <div className="rounded-full bg-gray-300 animate-pulse">
                                    　　　　
                                </div>
                            </div>
                            <button 
                                className="rounded-full bg-gray-300 animate-pulse px-6 py-2 text-white font-semibold "
                            >
                                　　　　
                            </button>
                        </div>
                    

                </div>
            </div>
        </div>
    )
}

export default SkeletonSettingBody;