export const ROUTES = {
    HOME: '/',
    HOME_VIEWS: '/views',
    HOME_LATEST: '/latest',
    HOME_LIKES: '/likes',
    SETTING: '/setting',
    POST_WRITE: '/write',
    POST: '/post',
    SEARCH: '/search',
    BLOG: '/blog',
    HISTORY: '/history',
    HISTORY_LIKED: '/history/liked',
    HISTORY_COMMENTS: '/history/comments',
    PROFILE: '/profile',
    TEST: '/test',
};

export const DYNAMIC_ROUTES = {
    SEARCH: (keyword?: string ) => {
        const basePath = `/search`;
        return keyword ? `${basePath}?keyword=${encodeURIComponent(keyword)}` : basePath;
    },
    POST_DETAIL: (nickname: string = ":nickname", postUuid: string = ":postUuid") => `/user/${nickname}/posts/${postUuid}`,
    POST_REWRITE: (postUuid: string = ":id") => `/write?id=${postUuid}`,
    USER_BLOG: (nickname: string = ":nickname") => `/${nickname}/posts`,
    USER_BLOG_SEARCH: (nickname: string = ":nickname", keyword?: string) => {
        const basePath = `/${nickname}/search`;
        return keyword ? `${basePath}?keyword=${encodeURIComponent(keyword)}` : basePath;
    },
}