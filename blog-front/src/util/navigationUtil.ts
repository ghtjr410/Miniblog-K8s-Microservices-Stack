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
    const toPostDetail = (nickname: string, postUuid: string) => {
        navigate(DYNAMIC_ROUTES.POST_DETAIL(nickname, postUuid));
    }
    const toPostDetailPreRead = (nickname: string, postUuid: string, postDetailData: PostDetailData) => {
        navigate(DYNAMIC_ROUTES.POST_DETAIL(nickname, postUuid), {state: { postDetailData }});
    }

    const toPostRewrite = (postUuid: string) => {
        navigate(DYNAMIC_ROUTES.POST_REWRITE(postUuid));
    }
    const toUserBlog = (nickname: string) => {
        navigate(DYNAMIC_ROUTES.USER_BLOG(nickname));
    }

    return { toHome, toHomeViews, toHomeLatest, toHomeLikes, toSearch, toPostWrite, toHistory, toHistoryLiked, toHistoryComments, toSetting, toPostDetail, toPostDetailPreRead, toPostRewrite, toUserBlog }
}

export default useNavigationHelper;