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

export function refValueToMap(refValue){
    return {
        page : refValue.get("page") , end : refValue.get("end") ,
        start : refValue.get("start") , keyword : refValue.get("keyword") ,
        contentCategoryId : refValue.get("contentCategoryId")
    };
}