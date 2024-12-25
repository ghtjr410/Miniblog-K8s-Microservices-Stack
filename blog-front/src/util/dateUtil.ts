export const formatDate = (isoDate: string): string => {
    if (isoDate === "") {
        return "";
    }
    const targetDate = new Date(isoDate);
    const now = new Date();
    const diffInSeconds = Math.floor((now.getTime() - targetDate.getTime()) / 1000);

    if (diffInSeconds < 60 * 5) {
        return '방금 전';
    } else if (diffInSeconds < 60 * 60) {
        const minutes = Math.floor(diffInSeconds / 60);
        return `${minutes}분 전`;
    } else if (diffInSeconds < 60 * 60 * 24) {
        const hours = Math.floor(diffInSeconds / (60 * 60));
        return `약 ${hours}시간 전`;
    } else if (diffInSeconds < 60 * 60 * 24 * 7) {
        const days = Math.floor(diffInSeconds / (60 * 60 * 24));
        return `${days}일 전`;
    } else {
        return targetDate.toLocaleDateString('ko-KR', {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
        });
    }
};