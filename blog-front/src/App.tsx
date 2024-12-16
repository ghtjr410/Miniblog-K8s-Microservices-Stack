import React, { useEffect, useState } from 'react';
import { Routes, Route } from 'react-router-dom';
import Keycloak from 'keycloak-js';
import Header from './components/header/Header';
import HomePage from './pages/HomePage';
import SettingPage from './pages/SettingPage';
import PostDetailPage from './pages/PostDetailPage';
import SearchPage from './pages/SearchPage';
import BlogPage from './pages/BlogPage';
import HistoryPage from './pages/HistroyPage';
import PostEditPage from './pages/PostEditPage';
import ProfilePage from './pages/ProfilePage';
import TestPage from './pages/TestPage';
import { DYNAMIC_ROUTES, ROUTES } from './constants/routes';
import { KEYCLOAK_URL } from './util/apiUrl';

type KeycloakStatus = "loading" | "authenticated" | "unauthenticated";

function App() {
  const [keycloak, setKeycloak] = useState<Keycloak | null>(null);
  const [keycloakStatus, setKeycloakStatus] = useState<KeycloakStatus>("loading");
  
  useEffect(() => {
    const keycloakInstance = new Keycloak({
      url: KEYCLOAK_URL,
      realm: 'miniblog-realm',
      clientId: 'service-client',
    });
    keycloakInstance.init({
      onLoad: "check-sso",
      checkLoginIframe: true,
      pkceMethod: 'S256',
      checkLoginIframeInterval: 30,
      silentCheckSsoRedirectUri:undefined,
    })
    .then((authenticated) => {
      setKeycloak(keycloakInstance);
      setKeycloakStatus(authenticated ? "authenticated" : "unauthenticated");

      if (authenticated) {
        // 토큰 갱신 인터벌 설정
        const refreshInterval = setInterval(() => {
          keycloakInstance
            .updateToken(70)
            .then((refreshed) => {
              if (refreshed) {
                console.log('토큰이 갱신되었습니다.');
              } else {
                console.warn('토큰이 아직 유효합니다.');
              }
            })
            .catch(() => {
              console.error('토큰 갱신 실패, 로그인 페이지로 이동합니다.');
              keycloakInstance.login();
            });
        }, 60000);

        // 컴포넌트 언마운트 시 인터벌 정리
        return () => clearInterval(refreshInterval);
      }
    })
    .catch((error) => {
      console.error("keycloak 초기화 실패: ", error);
      setKeycloakStatus("unauthenticated");
    })
  }, []);

  return (
    <Routes>
      <Route element={<Header keycloak={keycloak}/>}>        
        {/* 게시글 */}
        {/* <Route path={ROUTES.POST} element={<PostDetailPage keycloak={keycloak}/>}/> */}
        {/* 검색 */}
        <Route path={ROUTES.SEARCH} element={<SearchPage/>}/>
        {/* 블로그 */}
        <Route path={ROUTES.BLOG} element={<BlogPage/>}/>
        {/* 읽기목록 */}
        <Route path={ROUTES.HISTORY} element={<HistoryPage/>}/>

        {/* <Route path={ROUTES.POST_EDIT} element={<PostEditPage keycloak={keycloak}/>}/> */}
        {/* 프로필 */}
        <Route path={ROUTES.PROFILE} element={<ProfilePage/>}/>
        {/* 테스트 */}
        <Route path={ROUTES.TEST} element={<TestPage/>}/>
      </Route>

      {/* 홈페이지 */}
      <Route path={ROUTES.HOME} element={<HomePage keycloak={keycloak}/>}/> 
      {/* 세팅 페이지 */}
      <Route path={ROUTES.SETTING} element={<SettingPage keycloak={keycloak}/>}/>
      {/* 게시글 작성 */}
      <Route path={ROUTES.POST_WRITE} element={<PostEditPage keycloak={keycloak} keycloakStatus={keycloakStatus}/>}/>
      {/* 게시글 재작성 */}
      <Route path={DYNAMIC_ROUTES.POST_REWRITE()} element={<PostEditPage keycloak={keycloak} keycloakStatus={keycloakStatus}/>}/>
      {/* 게시글 상세 페이지 */}
      <Route path={DYNAMIC_ROUTES.POST_DETAIL()} element={<PostDetailPage keycloak={keycloak} keycloakStatus={keycloakStatus}/>}/>
    </Routes>
  );
}

export default App;
