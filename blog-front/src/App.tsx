import React from 'react';
import { Routes, Route, BrowserRouter } from 'react-router-dom';
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


function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<Header />}>
          {/* 홈페이지 */}
          <Route path={ROUTES.HOME} element={<HomePage/>}/> 
          {/* 게시글 */}
          <Route path={ROUTES.POST} element={<PostPage/>}/>
          {/* 검색 */}
          <Route path={ROUTES.SEARCH} element={<SearchPage/>}/>
          {/* 블로그 */}
          <Route path={ROUTES.BLOG} element={<BlogPage/>}/>
          {/* 읽기목록 */}
          <Route path={ROUTES.HISTORY} element={<HistoryPage/>}/>
          {/* 게시글 작성 */}
          <Route path={ROUTES.POST_EDIT} element={<PostEditPage/>}/>
          {/* 프로필 */}
          <Route path={ROUTES.PROFILE} element={<ProfilePage/>}/>
          {/* 테스트 */}
          <Route path={ROUTES.TEST} element={<TestPage/>}/>
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
