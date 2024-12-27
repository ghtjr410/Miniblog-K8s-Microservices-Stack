import React, { useEffect, useState } from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import Keycloak from 'keycloak-js';
import HistoryPage from './pages/history/HistroyPage';
import PostEditPage from './pages/postEdit/PostEditPage';
import { DYNAMIC_ROUTES, ROUTES } from './constants/routes';
import { KEYCLOAK_URL } from './util/apiUrl';
import BlogSearchPage from './pages/search/BlogSearchPage';
import TestHomePage from './pages/home/HomePage.test';
import SearchPage from './pages/search/SearchPage';
import TestPostDetailPage from './pages/postDetail/PostDetailPage.test';
import TestSettingPage from './pages/setting/SettingPage.test';
import TestBlogPage from './pages/blog/BlogPage.test';

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
              window.location.reload();
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
      {/* 홈페이지 */}
      <Route path={ROUTES.HOME} element={<TestHomePage keycloak={keycloak} keycloakStatus={keycloakStatus}/>}/> 
      <Route path={ROUTES.HOME_VIEWS} element={<TestHomePage keycloak={keycloak} keycloakStatus={keycloakStatus}/>}/> 
      <Route path={ROUTES.HOME_LATEST} element={<TestHomePage keycloak={keycloak} keycloakStatus={keycloakStatus}/>}/> 
      <Route path={ROUTES.HOME_LIKES} element={<TestHomePage keycloak={keycloak} keycloakStatus={keycloakStatus}/>}/> 
      <Route path='/:type' element={<Navigate to={ROUTES.HOME}/>}/>
      {/* 전체 검색 페이지 */}
      <Route path={DYNAMIC_ROUTES.SEARCH()} element={<SearchPage keycloak={keycloak} keycloakStatus={keycloakStatus}/>}/>
      {/* 세팅 페이지 */}
      <Route path={ROUTES.SETTING} element={<TestSettingPage keycloak={keycloak} keycloakStatus={keycloakStatus}/>}/>
      {/* 히스토리 페이지 */}
      <Route path={ROUTES.HISTORY} element={<HistoryPage keycloak={keycloak} keycloakStatus={keycloakStatus}/>} />
      <Route path={ROUTES.HISTORY_LIKED} element={<HistoryPage keycloak={keycloak} keycloakStatus={keycloakStatus}/>} />
      <Route path={ROUTES.HISTORY_COMMENTS} element={<HistoryPage keycloak={keycloak} keycloakStatus={keycloakStatus}/>} />
      <Route path="/history/:type" element={<Navigate to={ROUTES.HISTORY} replace />} />
      {/* 게시글 작성 */}
      <Route path={ROUTES.POST_WRITE} element={<PostEditPage keycloak={keycloak} keycloakStatus={keycloakStatus}/>}/>
      {/* 게시글 재작성 */}
      <Route path={DYNAMIC_ROUTES.POST_REWRITE()} element={<PostEditPage keycloak={keycloak} keycloakStatus={keycloakStatus}/>}/>
      {/* 게시글 상세 페이지 */}
      <Route path={DYNAMIC_ROUTES.POST_DETAIL()} element={<TestPostDetailPage keycloak={keycloak} keycloakStatus={keycloakStatus}/>}/>
      {/* 블로그 페이지 */}
      <Route path={DYNAMIC_ROUTES.USER_BLOG()} element={<TestBlogPage keycloak={keycloak} keycloakStatus={keycloakStatus}/>}/>
      <Route path={DYNAMIC_ROUTES.USER_BLOG_VIEWS()} element={<TestBlogPage keycloak={keycloak} keycloakStatus={keycloakStatus}/>}/>
      <Route path={DYNAMIC_ROUTES.USER_BLOG_LATEST()} element={<TestBlogPage keycloak={keycloak} keycloakStatus={keycloakStatus}/>}/>
      <Route path={DYNAMIC_ROUTES.USER_BLOG_LIKES()} element={<TestBlogPage keycloak={keycloak} keycloakStatus={keycloakStatus}/>}/>
      {/* 블로그 검색 페이지 */}
      <Route path={DYNAMIC_ROUTES.USER_BLOG_SEARCH()} element={<BlogSearchPage keycloak={keycloak} keycloakStatus={keycloakStatus}/>}/>
    </Routes>
  );
}

export default App;
