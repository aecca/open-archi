(function () {
    const commons = (function () {
        const commons = function (options) {

        };

        Object.defineProperty(Array.prototype, "pushAll", {
            value: function () {
                for (let i = 0; i < arguments.length; i++) {
                    const to_add = arguments[i];
                    for (let n = 0; n < to_add.length; n += 300) {
                        push_apply(this, slice_call(to_add, n, n + 300));
                    }
                }
            }
        });

        const replaceAll = function (str, find, replace) {
            return str.replace(new RegExp(escapeRegExp(find), 'g'), replace);
        };

        const escapeRegExp = function (str) {
            return str.replace(/([.*+?^=!:${}()|\[\]\/\\])/g, "\\$1");
        };

        const push_apply = Function.apply.bind([].push);
        const slice_call = Function.call.bind([].slice);
        const isEmpty = function (o) {
            if (o === undefined || o === null) {
                return true;
            }
            if (isArray(o)) {
                return o.length === 0;
            } else {
                return Object.keys(o).length === 0 && o.constructor === Object
            }
        };
        const isArray = function (o) {
            return Object.prototype.toString.call(o) === '[object Array]';
        };
        const isObject = function (o) {
            return o !== null && !isArray(o) && typeof o === 'object';
        };
        const isScalar = function (o) {
            return (/number|boolean/).test(typeof o);
        };
        const isString = function (o) {
            return (/string/).test(typeof o);
        };
        const getObjectLength = function (object) {
            return Object.keys(object).length;
        };
        const traverse = function (x, firstStep, prefix) {
            const result = [];
            let innerResult;
            if (firstStep) {
                result[0] = "___id";
                innerResult = traverse(x, false, prefix);
                result.pushAll(innerResult);
            } else {
                if (isArray(x)) {
                    innerResult = traverseArray(x, prefix);
                    result.pushAll(innerResult);
                } else if (isObject(x)) {
                    if (x == "tag") {
                        console.log("1 " + x);
                    }
                    innerResult = traverseObject(x, prefix);
                    result.pushAll(innerResult);
                } else {
                    if (!x) {
                        if (prefix && prefix.endsWith('.')) {
                            result.push(prefix.substring(0, prefix.length - 1));
                        } else {
                            result.push(prefix);
                        }
                    } else if (!isObject(x)) {
                        if (prefix)
                            prefix = replaceAll(prefix, '[*]', '');
                        if (prefix.endsWith('.')) {
                            result.push(prefix.substring(0, prefix.length - 1));
                        } else {

                            result.push(prefix);
                        }
                    }

                }
            }
            return result;
        };

        const setValue = function (obj, path, value) {
            let schema = obj;
            const pList = path.split('.');
            const len = pList.length;
            for (let i = 0; i < len - 1; i++) {
                const elem = pList[i];
                if (!schema[elem]) {
                    schema[elem] = {}
                }
                schema = schema[elem];
            }
            schema[pList[len - 1]] = value.trim();
        };

        const put = function (url, data, contentType) {
            new Promise(function (resolve, reject) {
                $.ajax({
                    url: url,
                    data: JSON.stringify(data),
                    type: 'PUT',
                    dataType: "json",
                    crossDomain: true,
                    contentType: contentType,
                    xhr: function () {
                        return window.XMLHttpRequest === null || new window.XMLHttpRequest().addEventListener === null
                            ? new window.ActiveXObject("Microsoft.XMLHTTP")
                            : $.ajaxSettings.xhr();
                    }
                }).done(function (data) {
                        resolve(data);
                    }
                ).fail(function (data) {
                        reject(data);
                    }
                )
            });
        };
        const patch = function (url, data, contentType) {
            new Promise(function (resolve, reject) {
                $.ajax({
                    url: url,
                    data: JSON.stringify(data),
                    type: 'PATCH',
                    dataType: "json",
                    crossDomain: true,
                    contentType: contentType,
                    xhr: function () {
                        return window.XMLHttpRequest === null || new window.XMLHttpRequest().addEventListener === null
                            ? new window.ActiveXObject("Microsoft.XMLHTTP")
                            : $.ajaxSettings.xhr();
                    }
                }).done(function (data) {
                        resolve(data);
                    }
                ).fail(function (data) {
                        reject(data);
                    }
                )
            });
        };
        const post = function (url, data, contentType) {
            new Promise(function (resolve, reject) {
                $.ajax({
                    url: url,
                    data: JSON.stringify(data),
                    type: 'POST',
                    dataType: "json",
                    crossDomain: true,
                    contentType: contentType,
                    xhr: function () {
                        return window.XMLHttpRequest === null || new window.XMLHttpRequest().addEventListener === null
                            ? new window.ActiveXObject("Microsoft.XMLHTTP")
                            : $.ajaxSettings.xhr();
                    }
                }).done(function (data) {
                        resolve(data);
                    }
                ).fail(function (data) {
                        reject(data);
                    }
                )
            });
        };
        commons.prototype = {
            isArray: isArray,
            isObject: isObject,
            traverse: traverse,
            setValue: setValue,
            replaceAll: replaceAll,
            isEmpty: isEmpty,
            isScalar: isScalar,
            isString: isString,
            getObjectLength: getObjectLength,
            findValues: findValues,
            patch: patch,
            put: put,
            post: post
        };

        function traverseArray(arr, prefix) {
            const val = arr[0];
            let result = {};
            if (isArray(val)) {
                if (isObject(val)) {
                    result = traverseArray(val, prefix);
                }
            } else {
                result = traverse(val, false, prefix);
            }
            return result;
        }

        function traverseObject(obj, prefix) {
            const result = [];
            let innerResult = [];
            for (let key in obj) {
                if (obj.hasOwnProperty(key)) {
                    const innerObject = obj[key];
                    const isArray_ = isArray(innerObject);
                    const isObject_ = isObject(innerObject);
                    if (isArray_) {
                        innerResult = traverseArray(innerObject, !prefix ? (key + "[*].") : (prefix + key + "[*]."));
                        result.pushAll(innerResult);
                    } else if (isObject_) {
                        innerResult = traverse(innerObject, false, !prefix ? (key + ".") : (prefix + key + "."));
                        result.pushAll(innerResult);
                    } else if ("___id" !== key && "___s" !== key) {
                        if (prefix && prefix.endsWith('.')) {
                            result.push(prefix + key);
                        } else {
                            result.push(key);
                        }
                    }
                }
            }
            return result;
        }

        function findValues(obj, key) {
            return findValuesHelper(obj, key, []);
        }

        function findValuesHelper(obj, key, list) {
            let i;
            if (!obj) return list;
            if (obj instanceof Array) {
                for (i in obj) {
                    list = list.concat(findValuesHelper(obj[i], key, []));
                }
                return list;
            }
            if (obj[key]) list.push(obj[key]);

            if ((typeof obj === "object") && (obj !== null)) {
                let children = Object.keys(obj);
                if (children.length > 0) {
                    for (i = 0; i < children.length; i++) {
                        list = list.concat(findValuesHelper(obj[children[i]], key, []));
                    }
                }
            }
            return list;
        }

        return commons;
    })();

    if (typeof module !== 'undefined' && typeof module.exports !== 'undefined') {
        module.exports = commons;
    }
    else {
        if (typeof define === 'function' && define.amd) {
            define([], function () {
                return commons;
            });
        }
        else {
            window.commons = commons;
        }
    }

})();

function toRGBString(rgbObject) {
    return "rgb(" + rgbObject.r + "," + rgbObject.g + "," + rgbObject.b + ")";
}

function getContrastYIQ(hexcolor) {
    if (hexcolor.startsWith("#")) {
        hexcolor = hexcolor.substr(1);
    }
    const r = parseInt(hexcolor.substr(0, 2), 16);
    const g = parseInt(hexcolor.substr(2, 2), 16);
    const b = parseInt(hexcolor.substr(4, 2), 16);
    const yiq = ((r * 299) + (g * 587) + (b * 114)) / 1000;
    return (yiq >= 128) ? 'black' : 'white';
}

function complementRBG(color) {
    let w3color_ = w3color(color);
    if (w3color_.valid) {
        return getContrastYIQ(w3color_.toHexString());
        /*        let temprgb = w3color_.toRgb();
                let temphsv = RGB2HSV(temprgb);
                temphsv.hue = HueShift(temphsv.hue, 180.0);
                temprgb = HSV2RGB(temphsv);
                const color2 = toRGBString(temprgb);
                const convertedColor = w3color(color2);
                return convertedColor.toHexString();*/
    } else {
        return 'black';
    }
}

function RGB2HSV(rgb) {
    let hsv = {};
    let max = max3(rgb.r, rgb.g, rgb.b);
    let dif = max - min3(rgb.r, rgb.g, rgb.b);
    hsv.saturation = (max === 0.0) ? 0 : (100 * dif / max);
    if (hsv.saturation === 0) hsv.hue = 0;
    else if (rgb.r === max) hsv.hue = 60.0 * (rgb.g - rgb.b) / dif;
    else if (rgb.g === max) hsv.hue = 120.0 + 60.0 * (rgb.b - rgb.r) / dif;
    else if (rgb.b === max) hsv.hue = 240.0 + 60.0 * (rgb.r - rgb.g) / dif;
    if (hsv.hue < 0.0) hsv.hue += 360.0;
    hsv.value = Math.round(max * 100 / 255);
    hsv.hue = Math.round(hsv.hue);
    hsv.saturation = Math.round(hsv.saturation);
    return hsv;
}

// RGB2HSV and HSV2RGB are based on Color Match Remix [http://color.twysted.net/]
// which is based on or copied from ColorMatch 5K [http://colormatch.dk/]
function HSV2RGB(hsv) {
    let rgb = {};
    if (hsv.saturation === 0) {
        rgb.r = rgb.g = rgb.b = Math.round(hsv.value * 2.55);
    } else {
        hsv.hue /= 60;
        hsv.saturation /= 100;
        hsv.value /= 100;
        let i = Math.floor(hsv.hue);
        let f = hsv.hue - i;
        let p = hsv.value * (1 - hsv.saturation);
        let q = hsv.value * (1 - hsv.saturation * f);
        let t = hsv.value * (1 - hsv.saturation * (1 - f));
        switch (i) {
            case 0:
                rgb.r = hsv.value;
                rgb.g = t;
                rgb.b = p;
                break;
            case 1:
                rgb.r = q;
                rgb.g = hsv.value;
                rgb.b = p;
                break;
            case 2:
                rgb.r = p;
                rgb.g = hsv.value;
                rgb.b = t;
                break;
            case 3:
                rgb.r = p;
                rgb.g = q;
                rgb.b = hsv.value;
                break;
            case 4:
                rgb.r = t;
                rgb.g = p;
                rgb.b = hsv.value;
                break;
            default:
                rgb.r = hsv.value;
                rgb.g = p;
                rgb.b = q;
        }
        rgb.r = Math.round(rgb.r * 255);
        rgb.g = Math.round(rgb.g * 255);
        rgb.b = Math.round(rgb.b * 255);
    }
    return rgb;
}

//Adding HueShift via Jacob (see comments)
function HueShift(h, s) {
    h += s;
    while (h >= 360.0) h -= 360.0;
    while (h < 0.0) h += 360.0;
    return h;
}

//min max via Hairgami_Master (see comments)
function min3(a, b, c) {
    return (a < b) ? ((a < c) ? a : c) : ((b < c) ? b : c);
}

function max3(a, b, c) {
    return (a > b) ? ((a > c) ? a : c) : ((b > c) ? b : c);
}