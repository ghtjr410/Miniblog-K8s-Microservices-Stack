const PostNotFoundModal = () => {
    const handleRefreshToHome = () => {
        window.location.href = "/";
    }
    return (
        <>
        <div className="remote-button fixed inset-0 bg-black opacity-25 z-30">
        </div>
        <div className="remote-button fixed inset-0 flex items-center justify-center bg-transparent z-50">
            {/* 모달 컨테이너 */}
            <div className="bg-gray-100 rounded-md shadow-md w-full max-w-sm mx-4 p-6">
                {/* 제목 */}
                <h3 className="text-xl font-bold mb-4">요청 실패</h3>
                {/* 설명 문구 */}
                <p className="mb-6 text-gray-700 text-sm">해당 게시글 데이터를 불러오는데 실패하였습니다.</p>

                {/* 버튼 영역 */}
                <div className="flex justify-end gap-2">
                    <button
                        className="px-4 py-2 text-sm font-medium rounded transition bg-green-500 hover:bg-green-600 text-white"
                        onClick={handleRefreshToHome}>
                        <span>확인</span>
                    </button>
                </div>
            </div>
        </div>
        </>
    )
}

export default PostNotFoundModal;