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
    const toHome = () => navigate(ROUTES.HOME);
    const toSetting = () => navigate(ROUTES.SETTING);
    const toPostWrite = () => navigate(ROUTES.POST_WRITE);
    
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

    return { toHome, toPostWrite, toSetting, toPostDetail, toPostDetailPreRead, toPostRewrite, toUserBlog }
}

export default useNavigationHelper;