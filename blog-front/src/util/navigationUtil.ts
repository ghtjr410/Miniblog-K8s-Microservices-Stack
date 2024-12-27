import { useNavigate } from "react-router-dom"
import { DYNAMIC_ROUTES, ROUTES } from "../constants/routes";

interface PostDetailData{
    postUuid: string;
    userUuid: string;
    nickname: string;
    title: string;
    content: string;
    createdDate: string;
    updatedDate: string;
    viewcount: number;
    likeCount: number;
    commentCount: number;
  }

const useNavigationHelper = () => {
    const navigate = useNavigate();
    // 정적
    const toBack = () => navigate(-1)
    // 홈
    const toHome = () => navigate(ROUTES.HOME);
    const toHomeViews = () => navigate(ROUTES.HOME_VIEWS);
    const toHomeLatest = () => navigate(ROUTES.HOME_LATEST);
    const toHomeLikes = () => navigate(ROUTES.HOME_LIKES);

    const toSearch = () => navigate(ROUTES.SEARCH);
    const toSetting = () => navigate(ROUTES.SETTING);
    const toPostWrite = () => navigate(ROUTES.POST_WRITE);
    
    // 히스토리
    const toHistory = () => navigate(ROUTES.HISTORY);
    const toHistoryLiked = () => navigate(ROUTES.HISTORY_LIKED);
    const toHistoryComments = () => navigate(ROUTES.HISTORY_COMMENTS);
    
    // 동적
    // 게시글 상세페이지
    const toPostDetail = (nickname: string, postUuid: string) => {
        navigate(DYNAMIC_ROUTES.POST_DETAIL(nickname, postUuid));
    }
    // 게시글 상세페이지 (최종적 일관성)
    const toPostDetailPreRead = (nickname: string, postUuid: string, postDetailData: PostDetailData) => {
        navigate(DYNAMIC_ROUTES.POST_DETAIL(nickname, postUuid), {state: { postDetailData }});
    }
    // 게시글 수정페이지
    const toPostRewrite = (postUuid: string) => {
        navigate(DYNAMIC_ROUTES.POST_REWRITE(postUuid));
    }
    // 유저 블로그
    const toUserBlog = (nickname: string) => {
        navigate(DYNAMIC_ROUTES.USER_BLOG(nickname));
    }
    // 유저 블로그 조회수 정렬
    const toUserBlogViews = (nickname: string) => {
        navigate(DYNAMIC_ROUTES.USER_BLOG_VIEWS(nickname));
    }
    // 유저 블로그 최신 정렬
    const toUserBlogLatest = (nickname: string) => {
        navigate(DYNAMIC_ROUTES.USER_BLOG_LATEST(nickname));
    }
    // 유저 블로그 좋아요 정렬
    const toUserBlogLikes = (nickname: string) => {
        navigate(DYNAMIC_ROUTES.USER_BLOG_LIKES(nickname));
    }

    const toUserBlogSearch = (nickname: string, keyword?: string) => {
        navigate(DYNAMIC_ROUTES.USER_BLOG_SEARCH(nickname, keyword));
    }

    return { toBack, toHome, toHomeViews, toHomeLatest, toHomeLikes, toSearch, toPostWrite, toHistory, 
        toHistoryLiked, toHistoryComments, toSetting, toPostDetail, toPostDetailPreRead, toPostRewrite, toUserBlog,
        toUserBlogSearch,
        toUserBlogViews, toUserBlogLatest, toUserBlogLikes }
}

export default useNavigationHelper;