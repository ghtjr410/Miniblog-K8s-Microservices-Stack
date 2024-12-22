import React from "react"; 


import useNavigationHelper from "../util/navigationUtil";


const TestPage = () => {
    const { toHome, toPostWrite, toPostDetail } = useNavigationHelper();

    return (
        <div className="bg-slate-100 flex justify-center ">
            <div className="bg-slate-200 max-w-screen-2xl w-full m-4 flex flex-col">
                <div className="text-3xl font-bold text-center m-4">
                    테스트 페이지
                </div>
                <div className="bg-slate-300 m-4 flex flex-col gap-4 border border-gray-500 rounded-md">
                    <div className="text-xl font-bold text-center">
                        비로그인 유저가 갈 수 있는 범위
                    </div>
                    <div className="bg-slate-400 flex gap-4 p-4">
                        <button 
                            className="bg-yellow-400 px-6 py-3 rounded-2xl"
                            onClick={() => toHome()}
                        >
                            Home
                        </button>
                        <button className="bg-yellow-400 px-6 py-3 rounded-2xl">Post</button>
                        <button className="bg-yellow-400 px-6 py-3 rounded-2xl">Blog</button>
                        <button className="bg-yellow-400 px-6 py-3 rounded-2xl">Search</button>
                        <button 
                            className="bg-yellow-400 px-6 py-3 rounded-2xl"
                            onClick={() => toPostDetail("nickname123", "post-1234")}
                        >
                                게시글 상세페이지
                        </button>
                    </div>
                </div>
                <div className="bg-slate-300 m-4 flex flex-col gap-4 border border-gray-500 rounded-md">
                    <div className="text-xl font-bold text-center">
                        로그인 유저가 갈 수 있는 범위
                    </div>
                    <div className="bg-slate-400 flex gap-4 p-4">
                        <button 
                            className="bg-yellow-400 px-6 py-3 rounded-2xl"
                            onClick={() => toHome()}
                        >
                            Home
                        </button>
                        <button className="bg-yellow-400 px-6 py-3 rounded-2xl">Post</button>
                        <button className="bg-yellow-400 px-6 py-3 rounded-2xl">Blog</button>
                        <button className="bg-yellow-400 px-6 py-3 rounded-2xl">Search</button>
                        <button 
                            className="bg-blue-400 px-6 py-3 rounded-2xl"
                            onClick={() => toPostWrite()}
                        >
                            Post Edit
                        </button>
                        <button className="bg-blue-400 px-6 py-3 rounded-2xl">Profile</button>
                        <button className="bg-blue-400 px-6 py-3 rounded-2xl">History</button>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default TestPage;