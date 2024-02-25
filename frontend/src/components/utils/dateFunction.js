/**
 * 오늘 날짜를 구해주는 함수.
 * @param change number (ex.-1 , 2)
 * @returns {string} 오늘 날짜를 문자열로 반환.
 */
export function dateThatCanChangeYear(change){
    let now = new Date();
    let year = now.getFullYear();
    let month = now.getMonth();
    let day = now.getDate();
    if(change != null){
        year = year + Number(change);
    }
    if(month < 10){ month = "0" + month}
    if(day < 10 ){day = "0" + day}

    return year + "-" + month + "-" + day;
}