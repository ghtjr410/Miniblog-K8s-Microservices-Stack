export const ROUTES = {
    HOME: '/',
    HOME_VIEWS: '/views',
    HOME_LATEST: '/latest',
    HOME_LIKES: '/likes',
    SETTING: '/setting',
    POST_WRITE: '/write',
    SEARCH: '/search',
    HISTORY: '/history',
    HISTORY_LIKED: '/history/liked',
    HISTORY_COMMENTS: '/history/comments',
    TEST: '/test',
    AAA:'aaa',
};

export const DYNAMIC_ROUTES = {
    SEARCH: (keyword?: string ) => {
        const basePath = `/search`;
        return keyword ? `${basePath}?keyword=${encodeURIComponent(keyword)}` : basePath;
    },
    POST_DETAIL: (nickname: string = ":nickname", postUuid: string = ":postUuid") => `/user/${nickname}/posts/${postUuid}`,
    POST_REWRITE: (postUuid: string = ":id") => `/write?id=${postUuid}`,
    USER_BLOG: (nickname: string = ":nickname") => `/user/${nickname}/posts`,
    USER_BLOG_VIEWS: (nickname: string = ":nickname") => `/user/${nickname}/posts/views`,
    USER_BLOG_LATEST: (nickname: string = ":nickname") => `/user/${nickname}/posts/latest`,
    USER_BLOG_LIKES: (nickname: string = ":nickname") => `/user/${nickname}/posts/likes`,
    USER_BLOG_SEARCH: (nickname: string = ":nickname", keyword?: string) => {
        const basePath = `/${nickname}/search`;
        return keyword ? `${basePath}?keyword=${encodeURIComponent(keyword)}` : basePath;
    },
}