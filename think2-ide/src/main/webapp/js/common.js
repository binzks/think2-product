/**
 * 判断对象是否为空
 * @param obj 对象
 * @returns {boolean}
 */
function isEmptyObject(obj) {
    for (var key in obj) {
        return false;
    }
    return true;
}
