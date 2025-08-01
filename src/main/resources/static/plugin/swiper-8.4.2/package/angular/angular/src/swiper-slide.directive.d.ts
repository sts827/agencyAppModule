import * as i0 from '@angular/core';
import {TemplateRef} from '@angular/core';

export declare class SwiperSlideDirective {
    template: TemplateRef<any>;
    virtualIndex: number;
    class: string;
    set ngClass(val: string);
    autoplayDelay: string | null;
    set zoom(val: boolean);
    get zoom(): boolean;
    private _zoom;
    slideIndex: number;
    get classNames(): string;
    set classNames(val: string);
    private _hasClass;
    slideData: {
        isActive: boolean;
        isPrev: boolean;
        isNext: boolean;
        isVisible: boolean;
        isDuplicate: boolean;
    };
    private _classNames;
    constructor(template: TemplateRef<any>);
    static ɵfac: i0.ɵɵFactoryDeclaration<SwiperSlideDirective, never>;
    static ɵdir: i0.ɵɵDirectiveDeclaration<SwiperSlideDirective, "ng-template[swiperSlide]", never, { "virtualIndex": "virtualIndex"; "class": "class"; "ngClass": "ngClass"; "autoplayDelay": "data-swiper-autoplay"; "zoom": "zoom"; }, {}, never>;
}
