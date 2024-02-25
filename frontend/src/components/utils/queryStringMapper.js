/**
 * 쿼리스트링 제작을 위해 제공.
 * @param categoryId contentCategoryId 의 값.
 * @param keyword 제목 검색 값.
 * @param start 검색 시작 날짜
 * @param end 검색 종료 날짜
 * @param page offset 으로 검색할 페이지 내용.
 * @returns {string} ?는 포함되지 않았지만 유효한 조건을 &로 연결해 반환.
 */
export function queryMapper(categoryId , keyword , start , end , page ){
    const params = new Map();

    params.set("contentCategoryId" , categoryId);
    params.set("keyword" , keyword);
    params.set("start" , start);
    params.set("end" , end);
    params.set("page" , page);

    let notNulls = [];

    console.log(params);
    for(const [key , value] of params){
        console.log("queryMapper key=" + key + "params[key]=" +value);
        if((value??null) !== null){
            notNulls.push(key + "=" + value);
        }
    }
    console.log(notNulls);
    return notNulls.join("&");
}