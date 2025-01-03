import React from 'react';

interface ApiFailModalProps {
    onClose: () => void;
}

const ApiFailModal: React.FC<ApiFailModalProps> = ({ onClose }) => {
    return (
        <>
        <div className="remote-button fixed inset-0  bg-black opacity-25  z-30">
        </div>
        <div className="remote-button fixed inset-0 flex items-center justify-center bg-transparent  z-50">
            {/* 모달 컨테이너 */}
            <div className="bg-gray-100 rounded-md shadow-md w-full max-w-sm mx-4 p-6 ">
                {/* 제목 */}
                <h3 className="text-xl font-bold mb-4">요청실패</h3>
                {/* 설명 문구 */}
                <p className="mb-6 text-gray-700">잠시후에 다시 이용해주세요</p>

                {/* 버튼 영역 */}
                <div className="flex justify-end gap-2">
                    <button
                        className="px-4 py-2 text-sm font-medium rounded transition bg-green-500 hover:bg-green-600 text-white"
                        onClick={onClose}>
                        <span>확인</span>
                    </button>
                </div>
            </div>
        </div>
    </>
    )
}

export default ApiFailModal;