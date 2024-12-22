const SkeletonBlogBody = () => {
    return (
        <div className="min-w-full min-h-screen flex justify-center bg-gray-100">
            <div className="flex-1 flex justify-center bg-white shadow-lg max-w-1728 pt-20 pb-4 px-4 2xl:max-x-1376 xl:max-w-1024">
                <div className="relative pt-32 pb-16 w-[768px] xs:w-full px-4 bg-white flex flex-col">
                    <div className="grid grid-cols-2 gap-6 mx-auto xs:flex xs:flex-col">

                    
                        {Array.from({ length:20 }).map((_, index) => (
                            <div
                                key={index}
                                className="bg-white w-full aspect-[17/20] md:aspect-[20/19] xs:aspect-[20/16] flex flex-col rounded-lg shadow-lg overflow-hidden">
                                <div className="h-[167px] md:h-[255px] xs:h-[372px] bg-gray-300 animate-pulse"></div>
                                <div className="flex-1 flex-col p-4">
                                    <div className=" text-base mb-1  flex gap-2">
                                        <div className="bg-gray-300 rounded-xl animate-pulse">
                                            　　　　　　　　　
                                        </div>
                                        <div className="bg-gray-300 rounded-xl animate-pulse">
                                            　　　　　　 
                                        </div>
                                    </div>
                                    <div className="text-xs flex flex-col gap-2 mt-4">
                                        <div className="flex gap-2">
                                            <div className="bg-gray-300 rounded-xl animate-pulse">
                                            　　　　　　　　　
                                            </div>
                                            <div className="bg-gray-300 rounded-xl animate-pulse">
                                                　　　
                                            </div>
                                            <div className="bg-gray-300 rounded-xl animate-pulse">
                                                　　　　　　 
                                            </div>
                                        </div>
                                        <div className="flex gap-2">
                                            <div className="bg-gray-300 rounded-xl animate-pulse">
                                            　　　　　　　　　
                                            </div>
                                            <div className="bg-gray-300 rounded-xl animate-pulse">
                                                　　　　　　 
                                            </div>
                                            <div className="bg-gray-300 rounded-xl animate-pulse">
                                                　　　　　　 
                                            </div>
                                        </div>
                                        
                                        <div className="flex gap-2">
                                            <div className="bg-gray-300 rounded-xl animate-pulse">
                                            　　　　　　　　　　　　　　　　　　
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className=" flex flex-col justify-end text-gray-500">
                                    <div className="px-4 py-2.5 text-xs flex">
                                        <div className="bg-gray-300 rounded-xl animate-pulse">
                                            　　　　　　　　　　　　
                                        </div>
                                    </div>
                                    <div className="px-4 py-2.5 flex justify-between text-xs border-t border-gray-200">
                                        <b className="bg-gray-300 rounded-xl animate-pulse">
                                            　　　　　　
                                        </b>
                                        <div className="flex gap-2">
                                            <div className="bg-gray-300 rounded-xl animate-pulse">
                                                　　　
                                            </div>
                                            <div className="bg-gray-300 rounded-xl animate-pulse">
                                                　　　
                                            </div>
                                            <div className="bg-gray-300 rounded-xl animate-pulse">
                                                　　　
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

export default SkeletonBlogBody;