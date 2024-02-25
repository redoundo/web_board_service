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