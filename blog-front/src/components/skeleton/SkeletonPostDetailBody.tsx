const SkeletonPostDetailBody = () => {
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

                    <div className="text-lg xs:text-base mb-16 flex flex-col">
                        <div className="px-6 xs:px-4 py-1 h-10 xs:h-10 mb-6 flex items-center  w-full bg-gray-300 animate-pulse rounded-full">
                            
                            
                        </div>
                        <div className="px-6 xs:px-4 py-1 h-10 xs:h-10 mb-6 flex items-center  w-full bg-gray-300 animate-pulse rounded-full">
                            
                            
                        </div>
                        <div className="px-6 xs:px-4 py-1 h-10 xs:h-10 mb-6 flex items-center  w-full bg-gray-300 animate-pulse rounded-full">
                            
                            
                        </div>

                    </div>
                    

                </div>
            </div>
        </div>
    )
}

export default SkeletonPostDetailBody;