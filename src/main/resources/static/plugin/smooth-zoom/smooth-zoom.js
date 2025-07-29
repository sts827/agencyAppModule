(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? module.exports = factory() :
        typeof define === 'function' && define.amd ? define(factory) :
            (global = typeof globalThis !== 'undefined' ? globalThis : global || self, global.Zoom = factory());
})(this, (function () { 'use strict';

    /*
     *
     *  Source of the code below
     *  https://stackoverflow.com/a/2541680
     *
     */
    function getAverageRGB(img, width, height) {
        const blockSize = 20;
        const rgb = { r: 0, g: 0, b: 0 };
        const canvas = document.createElement("canvas");
        const ctx = canvas.getContext("2d");
        const optimizedWidth = Math.sqrt(width);
        const optimizedHeight = Math.sqrt(height);
        if (!ctx) {
            return rgb;
        }
        canvas.width = optimizedWidth;
        canvas.height = optimizedHeight;
        try {
            ctx.drawImage(img, 0, 0, optimizedWidth, optimizedHeight);
            const imageData = ctx.getImageData(0, 0, optimizedWidth, optimizedHeight);
            const { data } = imageData;
            const { length } = data;
            const count = length / blockSize;
            for (let i = 0; i < length; i += blockSize) {
                rgb.r += data[i];
                rgb.g += data[i + 1];
                rgb.b += data[i + 2];
            }
            rgb.r = Math.floor(rgb.r / count);
            rgb.g = Math.floor(rgb.g / count);
            rgb.b = Math.floor(rgb.b / count);
        }
        catch (_a) {
            return rgb;
        }
        return rgb;
    }

    function Zoom(target, { background, useMaximumSize = true, onTransitionEnd, onClick, } = {}) {
        const zoom = (image) => {
            const src = image.currentSrc || image.src;
            const { srcset, naturalWidth } = image;
            const { offsetWidth: screenWidth, clientHeight: screenHeight } = document.documentElement;
            const { width, height, left, top } = image.getBoundingClientRect();
            const wrapX = screenWidth / 2 - left - width / 2;
            const wrapY = -top + (screenHeight - height) / 2;
            const maxScale = Math.min(screenWidth / width, screenHeight / height);
            const sizes = srcset.match(/ ([0-9]+)w/gm) || [];
            const maxWidth = useMaximumSize
                ? Math.max(naturalWidth, ...sizes
                    .map((x) => +x.trim().replace("w", ""))
                    .filter((x) => !Number.isNaN(x) && naturalWidth < x))
                : naturalWidth;
            const imageScale = maxWidth / width;
            const scale = Math.min(maxScale, imageScale);
            const bg = document.createElement("div");
            const imageClone = document.createElement("img");
            const removeImage = () => {
                bg.classList.remove("zoom-bg--reveal");
                imageClone.style.transform = "";
                imageClone.addEventListener("transitionend", () => {
                    bg.remove();
                    image.classList.remove("zoom-original--hidden");
                    imageClone.remove();
                }, { once: true });
                bg.removeEventListener("click", removeImage);
                imageClone.removeEventListener("click", removeImage);
                window.removeEventListener("scroll", removeImage);
                window.removeEventListener("resize", removeImage);
            };
            bg.classList.add("zoom-bg");
            if (background) {
                if (background === "auto") {
                    const { r, g, b } = getAverageRGB(image, width, height);
                    bg.style.backgroundColor = `rgba(${r}, ${g}, ${b}, 0.95)`;
                }
                else {
                    bg.style.background = background;
                }
            }
            imageClone.classList.add("zoom-img");
            imageClone.src = src;
            imageClone.width = width;
            imageClone.height = height;
            imageClone.style.top = `${top + window.scrollY}px`;
            imageClone.style.left = `${left}px`;
            imageClone.style.width = `${width}px`;
            imageClone.style.height = `${height}px`;
            bg.addEventListener("click", removeImage, { once: true });
            imageClone.addEventListener("click", removeImage, { once: true });
            imageClone.addEventListener("transitionend", () => {
                onTransitionEnd === null || onTransitionEnd === void 0 ? void 0 : onTransitionEnd(imageClone);
            }, { once: true });
            window.addEventListener("scroll", removeImage, {
                once: true,
                passive: true,
            });
            window.addEventListener("resize", removeImage, {
                once: true,
                passive: true,
            });
            document.body.append(bg, imageClone);
            // Hide original image
            image.classList.add("zoom-original--hidden");
            // For transition
            window.requestAnimationFrame(() => {
                window.setTimeout(() => {
                    imageClone.style.transform = `matrix(${scale}, 0, 0, ${scale}, ${wrapX}, ${wrapY})`;
                    bg.classList.add("zoom-bg--reveal");
                });
            });
        };
        const handleClick = (event) => {
            onClick === null || onClick === void 0 ? void 0 : onClick(event.target);
            zoom(event.target);
        };
        const attach = (target) => {
            if (!target) {
                return;
            }
            if (typeof target === "string") {
                document.querySelectorAll(target).forEach(addZoomEvent);
                // Add cursor style for target
                const style = document.createElement("style");
                const { head } = document;
                style.appendChild(document.createTextNode(`${target}{cursor:zoom-in}`));
                head.appendChild(style);
                return;
            }
            if (target instanceof HTMLElement) {
                addZoomEvent(target);
                return;
            }
            target.forEach(addZoomEvent);
        };
        const addZoomEvent = (element) => {
            var _a;
            if (!(element instanceof HTMLElement)) {
                return;
            }
            if (element.tagName === "IMG") {
                element.addEventListener("click", handleClick);
                return;
            }
            (_a = element.querySelector("img")) === null || _a === void 0 ? void 0 : _a.addEventListener("click", handleClick);
        };
        if (target) {
            attach(target);
        }
        return {
            zoom,
            attach,
        };
    }

    function styleInject(css, ref) {
        if (ref === void 0) ref = {};
        var insertAt = ref.insertAt;

        if (!css || typeof document === 'undefined') {
            return;
        }

        var head = document.head || document.getElementsByTagName('head')[0];
        var style = document.createElement('style');
        style.type = 'text/css';

        if (insertAt === 'top') {
            if (head.firstChild) {
                head.insertBefore(style, head.firstChild);
            } else {
                head.appendChild(style);
            }
        } else {
            head.appendChild(style);
        }

        if (style.styleSheet) {
            style.styleSheet.cssText = css;
        } else {
            style.appendChild(document.createTextNode(css));
        }
    }

    var css_248z = ".zoom-bg,.zoom-img{cursor:zoom-out;z-index:90000}.zoom-img{display:block;position:absolute;transition:transform .3s ease-in-out}.zoom-bg{background-color:rgba(0,0,0,.95);height:100vh;left:0;opacity:0;position:fixed;top:0;transition:opacity .3s ease-in-out;width:100vw}.zoom-bg.zoom-bg--reveal{opacity:1}.zoom-original--hidden{visibility:hidden}";
    styleInject(css_248z);

    return Zoom;

}));
