export function toMap(list){
    const arr = Array.from(list);
    let mapped = null;
    if(arr != null && arr.length > 0){
        mapped = arr.reduce((map , obj) =>{
            map.set(obj.key , obj.value);
            return map;
        }, new Map)
    }
    return mapped;
}

/**
 * qs 라이브러리를 사용하기 위해 map 을 object 로 바꾸는 작업
 * @param refValue ref 값
 * @returns {{start: *, end: *, contentCategoryId: *, page: *, keyword: *}}
 */
export function refValueToMap(refValue){
    return {
        page : refValue.get("page") , end : refValue.get("end") ,
        start : refValue.get("start") , keyword : refValue.get("keyword") ,
        contentCategoryId : refValue.get("contentCategoryId")
    };
}