import React, { useEffect, useState } from "react"; 
import Keycloak from "keycloak-js";
import axios from "axios";
import SkeletonCommonHeader from "../../components/skeleton/SkeletonCommonHeader";
import SkeletonSettingBody from "../../components/skeleton/SkeletonSettingBody";
import Header from "../../components/header/common/Header";
import { IoWarningOutline } from "react-icons/io5";
import { getNicknameProfile } from "../../service/queryService.auth";
import { API_PROFILE_CREATE_OR_UPDATE_URL } from "../../util/apiUrl";
import { deleteAccount } from "../../service/accountService";
import AuthRefreshModal from "../../components/modal/AuthRefreshModal";
import ApiFailModal from "../../components/modal/ApiFailModal";

interface Props {
    keycloak: Keycloak | null;
    keycloakStatus: "loading" | "authenticated" | "unauthenticated";
}

interface KeycloakProfile {
    id?: string;
    username?: string;
    firstName?: string;
    lastName?: string;
    email?: string;
    attributes?: { [key: string]: string | string[] };
}

interface UserInfo extends KeycloakProfile {
    preferred_username?: string;
    nickname?: string;
}

const TestSettingPage: React.FC<Props> = ({ keycloak, keycloakStatus }) => {
    const [name, setName] = useState<string>("");
    const [nickname, setNickname] = useState<string>("");
    const [email, setEmail] = useState<string>("");

    // 블로그 제목 상태 관리
    const [isEditingTitle, setIsEditingTitle] = useState<boolean>(false);
    const [editTitle, setEditTitle] = useState<string>("");
    const [title, setTitle] = useState<string>("");

    // 블로그 소개 상태 관리
    const [isEditingIntro, setIsEditingIntro] = useState<boolean>(false);
    const [editIntro, setEditIntro] = useState<string>("");
    const [intro, setIntro] = useState<string>("");

    const [isModalLoading, setIsModalLoading] = useState(false);
    const [isModalOpen, setIsModalOpen] = useState(false);

    const [isAuthRefreshModalOpen, setIsAuthRefreshModalOpen] = useState(false);
    const [isApiFaileModalOpen, setIsApiFaileModalOpen] = useState(false);

    useEffect(() => {
        if (keycloakStatus === "unauthenticated") {
            setIsAuthRefreshModalOpen(true);
        } else if (keycloakStatus === "authenticated" && keycloak && keycloak.authenticated) {
            // 사용자 정보 로드
            keycloak.loadUserInfo().then((info: UserInfo) => {
                console.log("User Info:", JSON.stringify(info));

                // userInfo 데이터를 개별 상태 변수에 설정
                setName(info.preferred_username || ""); // name 필드에 preferred_username 사용
                console.log("테스트 " + info.preferred_username);
                setNickname(info.nickname || "");
                setEmail(info.email || "");
                fetchNicknameProfile(info.nickname || "")
            });
        }
    }, [keycloak, keycloakStatus]);

    const fetchNicknameProfile = (nickname: string) => {
        getNicknameProfile(nickname)
        .then((response) => {
            console.log(response);
            setTitle(response.title);
            setEditTitle(response.title);
            setIntro(response.intro);
            setEditIntro(response.intro);
        })
        .catch((error) => {
            console.error("데이터가져오는데 실패", error);
            setTitle(`${nickname} 블로그`);
            setEditTitle(`${nickname}' 블로그`);
            setIntro("");
            setEditIntro("");
        })
    }

    // 블로그 제목 수정 핸들러
    const handleEditTitle = () => {
        setEditTitle(title); 
        setIsEditingTitle(true);
    };
    const handleEditTitleCancel = () => {
        setEditTitle(title);
        setIsEditingTitle(false)
    }
    const handleSaveTitle = async () => {
        const token = keycloak?.token;
        
        if (!token) {
            setIsAuthRefreshModalOpen(true);
            return;
        }

        axios.post(
            API_PROFILE_CREATE_OR_UPDATE_URL(),
            {
                nickname: nickname,
                email: email,
                title: editTitle,
                intro: intro,
            }, 
            {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
            }
        )
        .then(() => {
            setIsEditingTitle(false);
            setTitle(editTitle);
        })
        .catch(() => {
            setIsApiFaileModalOpen(true);
        })
    };

    // 블로그 소개 수정 핸들러
    const handleEditIntro = () => {
        setEditIntro(intro);
        setIsEditingIntro(true);
    };

    const handleEditIntroCancel = () => {
        setEditIntro(intro);
        setIsEditingIntro(false)
    }

    const handleSaveIntro = () => {
        const token = keycloak?.token;

        if (!token) {
            setIsAuthRefreshModalOpen(true);
            return;
        }

        axios.post(
            API_PROFILE_CREATE_OR_UPDATE_URL(),
            {
                nickname: nickname,
                email: email,
                title: title,
                intro: editIntro,
            }, 
            {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
            }
        )
        .then(() => {
            setIsEditingIntro(false);
            setIntro(editIntro);
        })
        .catch(() => {
            setIsApiFaileModalOpen(true);
        })
    };

    // 회원탈퇴 핸들러
    const handleDeleteAccount = () => {
        const token = keycloak?.token;
        
        if (!token) {
            setIsAuthRefreshModalOpen(true);
            return;
        }

        deleteAccount(token)
        .then(() => {
            handleConfirmDelete();
        })
        .catch(() => {
            setIsModalOpen(false);
            setIsApiFaileModalOpen(true);
        })
    };
    // 모달 열기
    const handleModalOpen = () => {
        setIsModalOpen(true);
    };

    // 모달 닫기
    const handleModalClose = () => {
        setIsModalOpen(false);
    };

    // 삭제 확정(예시)
    const handleConfirmDelete = async  () => {
        setIsModalLoading(true); // 로딩 시작

        // 3초 지연 (또는 실제 API 호출)
        setTimeout(() => {
            // 작업 완료 후 로딩 해제 + 모달 닫기
            setIsModalLoading(false);
            setIsModalOpen(false);
            window.location.href = "/";
        }, 3000);
    };

    const handleCloseApiFailModal = () => {
        setIsApiFaileModalOpen(false);
    }


    if (!keycloak) {
        return (
            <>
                <SkeletonCommonHeader/>
                <SkeletonSettingBody/>
            </>
        )
    }
    return (
        <>
            <Header keycloak={keycloak} keycloakStatus={keycloakStatus}/>
            {/* 전체 바디 (빈공간 포함) */}
            <div className="min-w-full min-h-screen flex justify-center  bg-gray-100">
                {/* 본문바디 (빈공간 미포함) */}
                <div className="max-w-1728 2xl:max-w-1376 xl:max-w-1024  flex-1 flex justify-center bg-white shadow-lg">
                    <div className="relative pt-32 pb-16 w-[768px] xs:w-full xs:px-4  2xs:px-2 bg-white flex flex-col">
                        <div className="flex border-b border-gray-200 py-4 xs:flex-col xs:gap-2">
                            <div className="text-xl font-bold w-40 flex items-center">
                                아이디
                            </div>
                            <input type="text" value={name} readOnly className="border border-gray-300 text-gray-500 rounded px-3 py-2 bg-gray-100 xs:w-[200px]"/>
                        </div>
                        <div className="flex border-b border-gray-200 py-4 xs:flex-col xs:gap-2">
                            <div className="text-xl font-bold w-40 flex items-center">
                                닉네임
                            </div>
                            <input type="text" value={nickname} readOnly className="border border-gray-300 text-gray-500 rounded px-3 py-2 bg-gray-100 xs:w-[200px]"/>
                        </div>
                        <div className="flex border-b border-gray-200 py-4 xs:flex-col xs:gap-2 ">
                            <div className="text-xl font-bold w-40 flex items-center">
                                이메일
                            </div>
                            <input type="text" value={email} readOnly className="border border-gray-300 text-gray-500 w-96 rounded px-3 py-2 bg-gray-100 xs:w-[300px]"/>
                        </div>
                        <div className="flex border-b border-gray-200 py-4 xs:flex-col xs:gap-2">
                            <div className="text-xl font-bold w-40 flex items-center">
                                블로그 제목
                            </div>
                            <div className="flex items-center gap-2 xs:flex xs:items-start xs:flex-col xs:gap-2">
                                <input 
                                    type="text" 
                                    value={editTitle} 
                                    maxLength={10}
                                    onChange={(e) => setEditTitle(e.target.value)} 
                                    readOnly={!isEditingTitle} 
                                    className={`border border-gray-300 rounded px-3 py-2 w-96 xs:w-full ${isEditingTitle ? "bg-white" : "bg-gray-100  text-gray-500"}`}
                                />
                                {isEditingTitle ? (
                                    <div className="xs:ml-auto">
                                        <button 
                                            className="px-6 py-1.5 text-lg rounded bg-green-400  font-bold text-white hover:bg-green-500 cursor-pointer"
                                            onClick={handleSaveTitle}>
                                            저장
                                        </button>
                                        <button 
                                            className="ml-2 px-6 py-1.5 text-lg rounded bg-red-400 font-bold text-white hover:bg-red-600 cursor-pointer"
                                            onClick={handleEditTitleCancel}>
                                            취소
                                        </button>
                                    </div>

                                ) : (
                                    <button className="px-6 py-1.5 text-lg rounded border border-black bg-white font-black hover:bg-black hover:text-white xs:ml-auto transition-colors duration-300 ease-in-out cursor-pointer"
                                        onClick={handleEditTitle}>
                                        수정
                                    </button>
                                )}
                            </div>
                        </div>
                        <div className="flex border-b border-gray-200 py-4 xs:flex-col xs:gap-2">
                            <div className="text-xl font-bold w-40 flex items-center">
                                블로그 소개
                            </div>
                            <div className="flex items-center gap-2 xs:flex xs:items-start xs:flex-col xs:gap-2">
                                <input 
                                    type="text" 
                                    value={editIntro} 
                                    maxLength={50}
                                    onChange={(e) => setEditIntro(e.target.value)} 
                                    readOnly={!isEditingIntro} 
                                    className={`border border-gray-300 rounded px-3 py-2 w-96 xs:w-full ${isEditingIntro ? "bg-white" : "bg-gray-100 text-gray-500"}`}
                                />
                                {isEditingIntro ? (
                                    <div className="xs:ml-auto">
                                        <button 
                                            className="px-6 py-1.5 text-lg rounded bg-green-400  font-bold text-white hover:bg-green-500"
                                            onClick={handleSaveIntro}>
                                            저장
                                        </button>
                                        <button 
                                            className="ml-2 px-6 py-1.5 text-lg rounded bg-red-400 font-bold text-white hover:bg-red-600"
                                            onClick={handleEditIntroCancel}>
                                            취소
                                        </button>
                                    </div>
                                ) : (
                                    <button className="px-6 py-1.5 text-lg rounded border border-black bg-white font-black hover:bg-black hover:text-white xs:ml-auto transition-colors duration-300 ease-in-out"
                                        onClick={handleEditIntro}>
                                        수정
                                    </button>
                                )}
                            </div>
                        </div>
                        <div className="flex border-b border-gray-200 py-4 xs:flex-col xs:gap-2">
                            <div className="text-xl font-bold w-40 flex items-center">
                                회원탈퇴
                            </div>
                            <button 
                                className="bg-red-400 px-6 py-1.5 text-lg rounded text-white font-semibold hover:bg-red-700 flex items-center justify-center gap-2"
                                onClick={handleModalOpen}>
                                <IoWarningOutline size={25}/>
                                회원탈퇴
                                <div>　</div>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            {isModalOpen && (
                <>
                    <div className="remote-button fixed inset-0  bg-black opacity-25  z-30">
                    </div>
                    <div className="remote-button fixed inset-0 flex items-center justify-center bg-transparent  z-50">
                        {/* 모달 컨테이너 */}
                        <div className="bg-gray-100 rounded-md shadow-md w-full max-w-sm mx-4 p-6 ">
                            {/* 제목 */}
                            <h3 className="text-xl font-bold mb-4">회원탈퇴</h3>
                            {/* 설명 문구 */}
                            <p className="mb-6 text-gray-700">정말로 탈퇴하시겠습니까?</p>

                            {/* 버튼 영역 */}
                            <div className="flex justify-end gap-2">
                            <button
                                className="px-4 py-2 text-sm font-medium text-gray-600 bg-gray-200 rounded hover:bg-gray-300 transition"
                                onClick={handleModalClose}
                            >
                                취소
                            </button>
                            <button
                                className={`px-4 py-2 text-sm font-medium rounded transition 
                                    ${isModalLoading ? 'bg-green-300 cursor-not-allowed' : 'bg-green-500 hover:bg-green-600 text-white'}
                                `}
                                onClick={handleDeleteAccount}
                                disabled={isModalLoading} // 로딩 중에는 클릭 불가능
                            >
                                {isModalLoading ? (
                                    // 로딩 중일 때 표시할 영역
                                    <div className="flex items-center gap-2">
                                    <div className="w-4 h-4 border-2 border-t-transparent border-white rounded-full animate-spin" />
                                    <span>처리 중...</span>
                                    </div>
                                ) : (
                                    // 평소 표시(확인 버튼)
                                    <span>확인</span>
                                )}
                            </button>
                            </div>
                        </div>
                    </div>
                </>
                
            )}
            {isAuthRefreshModalOpen && (
                <AuthRefreshModal/>
            )}
            {isApiFaileModalOpen && (
                <ApiFailModal onClose={handleCloseApiFailModal}/>
            )}
        </>
    )
}

export default TestSettingPage;