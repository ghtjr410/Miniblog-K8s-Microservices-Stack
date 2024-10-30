import React, { useEffect, useState } from 'react';
import { Routes, Route } from 'react-router-dom';
import Keycloak from 'keycloak-js';
import Header from './components/header/Header';
import HomePage from './pages/HomePage';
import PostPage from './pages/PostPage';
import SearchPage from './pages/SearchPage';
import BlogPage from './pages/BlogPage';
import HistoryPage from './pages/HistroyPage';
import PostEditPage from './pages/PostEditPage';
import ProfilePage from './pages/ProfilePage';
import TestPage from './pages/TestPage';
import { ROUTES } from './constants/routes';
import { KEYCLOAK_URL } from './util/apiUrl';

function App() {
  const [keycloak, setKeycloak] = useState<Keycloak | null>(null);

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
    })
  }, []);

  return (
    <Routes>
      <Route element={<Header keycloak={keycloak}/>}>
        {/* 홈페이지 */}
        <Route path={ROUTES.HOME} element={<HomePage/>}/> 
        {/* 게시글 */}
        <Route path={ROUTES.POST} element={<PostPage keycloak={keycloak}/>}/>
        {/* 검색 */}
        <Route path={ROUTES.SEARCH} element={<SearchPage/>}/>
        {/* 블로그 */}
        <Route path={ROUTES.BLOG} element={<BlogPage/>}/>
        {/* 읽기목록 */}
        <Route path={ROUTES.HISTORY} element={<HistoryPage/>}/>
        {/* 게시글 작성 */}
        {/* <Route path={ROUTES.POST_EDIT} element={<PostEditPage keycloak={keycloak}/>}/> */}
        {/* 프로필 */}
        <Route path={ROUTES.PROFILE} element={<ProfilePage/>}/>
        {/* 테스트 */}
        <Route path={ROUTES.TEST} element={<TestPage/>}/>
      </Route>
      <Route path={ROUTES.POST_EDIT} element={<PostEditPage keycloak={keycloak}/>}/>
    </Routes>
  );
}

export default App;
