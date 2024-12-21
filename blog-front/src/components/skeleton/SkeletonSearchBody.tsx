const SkeletonSearchBody = () => {
    return (
        <div className="min-w-full min-h-screen flex justify-center  bg-gray-100">
            {/* 본문(빈공간 미포함) */}
            <div className="max-w-1728 2xl:max-w-1376 xl:max-w-1024  flex-1 flex justify-center bg-white shadow-lg">
                {/* 검색 섹션 */}
                <div className="relative pt-32 pb-16 w-[768px] xs:w-full xs:px-4  2xs:px-2 bg-white flex flex-col">
                    {/* 검색 인풋 */}
                    <div className="px-6 xs:px-4 py-1 h-16 xs:h-10 mb-6 flex items-center  w-full bg-gray-300 animate-pulse rounded-full">
                        {/* <BsSearch className="mr-5 text-3xl xs:text-lg xs:mr-3"/> */}
                        
                    </div>
                    <div className="text-lg xs:text-base mb-16 flex">
                        <div className="rounded-full bg-gray-300 animate-pulse">
                            　　
                        </div>
                        <div className="ml-4 rounded-full bg-gray-300 animate-pulse">
                            　　　　
                        </div>
                        <div className="ml-4 rounded-full bg-gray-300 animate-pulse">
                            　　　　　　　　　　　　
                        </div>
                    </div>
                    <div className="flex flex-col gap-16">
                        {Array.from({ length: 5 }).map((_, index) => (
                            <div 
                                key={index}
                                className=" flex flex-col w-full h-auto overflow-hidden rounded-lg shadow-lg ">
                                <div className="min-h-[300px] md:min-h-[250px]  bg-gray-300 animate-pulse">
                                    <div className="w-full h-full">

                                    </div>
                                </div>
                                <div className="px-2 my-6 xs:my-2  text-2xl xs:text-lg flex flex-col">
                                    <div className="flex" >
                                        <div className="rounded-full bg-gray-300 animate-pulse">
                                        　　　　　　　　　
                                        </div>
                                        <div className="ml-4 rounded-full bg-gray-300 animate-pulse">
                                        　　　　
                                        </div>
                                    </div>
                                    
                                </div>
                                <div className="px-2 text-xs flex flex-col gap-2">
                                    <div className="flex">
                                        <div className="rounded-full bg-gray-300 animate-pulse">
                                            　　　　　
                                        </div>
                                        <div className="ml-4 rounded-full bg-gray-300 animate-pulse">
                                            　　　　　　　　　　　　
                                        </div>
                                        <div className="ml-4 rounded-full bg-gray-300 animate-pulse">
                                            　　　　　　　　　　　　
                                        </div>
                                    </div>
                                    <div className="flex">
                                        <div className="rounded-full bg-gray-300 animate-pulse">
                                            　　　　　　　　　　　　　　　　　　　　　　　　　　　
                                        </div>
                                        <div className="ml-4 rounded-full bg-gray-300 animate-pulse">
                                            　　　　
                                        </div>
                                    </div>
                                    <div className="flex">
                                        <div className=" rounded-full bg-gray-300 animate-pulse">
                                            　　　　　　　　　
                                        </div>
                                        <div className="ml-4 rounded-full bg-gray-300 animate-pulse">
                                            　　　　
                                        </div>
                                        <div className="ml-4 rounded-full bg-gray-300 animate-pulse">
                                            　　　　　　　　
                                        </div>
                                    </div>
                                </div>

                                <div className=" w-full flex flex-col text-gray-500
                                        2xs:h-auto 2xs:text-xs">
                                    <div className="px-2 mt-4 py-2.5 text-xs ">
                                        <div className="flex">
                                            <div className="rounded-full bg-gray-300 animate-pulse">
                                                　　　　　　　　　　　　
                                            </div>
                                        </div>
                                    </div>
                                    <div className="px-4 py-2.5 flex justify-between text-xs border-t border-gray-200 text-black ">
                                            <div className="rounded-full bg-gray-300 animate-pulse">
                                                　　　　　　　　　　　　
                                            </div>
                                        <div className="flex gap-2">
                                            <div className="flex items-center gap-1">
                                                <div className="rounded-full bg-gray-300 animate-pulse">
                                                　　　　　　
                                                </div>
                                            </div>
                                            <div className="flex items-center gap-1">
                                                <div className="rounded-full bg-gray-300 animate-pulse">
                                                　　　　　　
                                                </div>
                                            </div>
                                            <div className="flex items-center gap-1">
                                                <div className="rounded-full bg-gray-300 animate-pulse">
                                                　　　　　　
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        ))}

                    </div>
                </div>
            </div>
        </div>
    )
}

export default SkeletonSearchBody;