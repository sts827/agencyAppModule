import {h, nextTick, onBeforeUnmount, onMounted, onUpdated, provide, ref, watch} from 'vue';
import SwiperCore from 'swiper';
import {getParams} from '../components-shared/get-params.js';
import {mountSwiper} from '../components-shared/mount-swiper.js';
import {extend, needsNavigation, needsPagination, needsScrollbar, uniqueClasses} from '../components-shared/utils.js';
import {calcLoopedSlides, renderLoop} from './loop.js';
import {getChangedParams} from '../components-shared/get-changed-params.js';
import {getChildren} from './get-children.js';
import {updateSwiper} from '../components-shared/update-swiper.js';
import {renderVirtual} from './virtual.js';
import {updateOnVirtualData} from '../components-shared/update-on-virtual-data.js';

const Swiper = {
  name: 'Swiper',
  props: {
    tag: {
      type: String,
      default: 'div'
    },
    wrapperTag: {
      type: String,
      default: 'div'
    },
    modules: {
      type: Array,
      default: undefined
    },
    init: {
      type: Boolean,
      default: undefined
    },
    direction: {
      type: String,
      default: undefined
    },
    touchEventsTarget: {
      type: String,
      default: undefined
    },
    initialSlide: {
      type: Number,
      default: undefined
    },
    speed: {
      type: Number,
      default: undefined
    },
    cssMode: {
      type: Boolean,
      default: undefined
    },
    updateOnWindowResize: {
      type: Boolean,
      default: undefined
    },
    resizeObserver: {
      type: Boolean,
      default: undefined
    },
    nested: {
      type: Boolean,
      default: undefined
    },
    focusableElements: {
      type: String,
      default: undefined
    },
    width: {
      type: Number,
      default: undefined
    },
    height: {
      type: Number,
      default: undefined
    },
    preventInteractionOnTransition: {
      type: Boolean,
      default: undefined
    },
    userAgent: {
      type: String,
      default: undefined
    },
    url: {
      type: String,
      default: undefined
    },
    edgeSwipeDetection: {
      type: [Boolean, String],
      default: undefined
    },
    edgeSwipeThreshold: {
      type: Number,
      default: undefined
    },
    autoHeight: {
      type: Boolean,
      default: undefined
    },
    setWrapperSize: {
      type: Boolean,
      default: undefined
    },
    virtualTranslate: {
      type: Boolean,
      default: undefined
    },
    effect: {
      type: String,
      default: undefined
    },
    breakpoints: {
      type: Object,
      default: undefined
    },
    spaceBetween: {
      type: Number,
      default: undefined
    },
    slidesPerView: {
      type: [Number, String],
      default: undefined
    },
    maxBackfaceHiddenSlides: {
      type: Number,
      default: undefined
    },
    slidesPerGroup: {
      type: Number,
      default: undefined
    },
    slidesPerGroupSkip: {
      type: Number,
      default: undefined
    },
    slidesPerGroupAuto: {
      type: Boolean,
      default: undefined
    },
    centeredSlides: {
      type: Boolean,
      default: undefined
    },
    centeredSlidesBounds: {
      type: Boolean,
      default: undefined
    },
    slidesOffsetBefore: {
      type: Number,
      default: undefined
    },
    slidesOffsetAfter: {
      type: Number,
      default: undefined
    },
    normalizeSlideIndex: {
      type: Boolean,
      default: undefined
    },
    centerInsufficientSlides: {
      type: Boolean,
      default: undefined
    },
    watchOverflow: {
      type: Boolean,
      default: undefined
    },
    roundLengths: {
      type: Boolean,
      default: undefined
    },
    touchRatio: {
      type: Number,
      default: undefined
    },
    touchAngle: {
      type: Number,
      default: undefined
    },
    simulateTouch: {
      type: Boolean,
      default: undefined
    },
    shortSwipes: {
      type: Boolean,
      default: undefined
    },
    longSwipes: {
      type: Boolean,
      default: undefined
    },
    longSwipesRatio: {
      type: Number,
      default: undefined
    },
    longSwipesMs: {
      type: Number,
      default: undefined
    },
    followFinger: {
      type: Boolean,
      default: undefined
    },
    allowTouchMove: {
      type: Boolean,
      default: undefined
    },
    threshold: {
      type: Number,
      default: undefined
    },
    touchMoveStopPropagation: {
      type: Boolean,
      default: undefined
    },
    touchStartPreventDefault: {
      type: Boolean,
      default: undefined
    },
    touchStartForcePreventDefault: {
      type: Boolean,
      default: undefined
    },
    touchReleaseOnEdges: {
      type: Boolean,
      default: undefined
    },
    uniqueNavElements: {
      type: Boolean,
      default: undefined
    },
    resistance: {
      type: Boolean,
      default: undefined
    },
    resistanceRatio: {
      type: Number,
      default: undefined
    },
    watchSlidesProgress: {
      type: Boolean,
      default: undefined
    },
    grabCursor: {
      type: Boolean,
      default: undefined
    },
    preventClicks: {
      type: Boolean,
      default: undefined
    },
    preventClicksPropagation: {
      type: Boolean,
      default: undefined
    },
    slideToClickedSlide: {
      type: Boolean,
      default: undefined
    },
    preloadImages: {
      type: Boolean,
      default: undefined
    },
    updateOnImagesReady: {
      type: Boolean,
      default: undefined
    },
    loop: {
      type: Boolean,
      default: undefined
    },
    loopAdditionalSlides: {
      type: Number,
      default: undefined
    },
    loopedSlides: {
      type: Number,
      default: undefined
    },
    loopedSlidesLimit: {
      type: Boolean,
      default: true
    },
    loopFillGroupWithBlank: {
      type: Boolean,
      default: undefined
    },
    loopPreventsSlide: {
      type: Boolean,
      default: undefined
    },
    rewind: {
      type: Boolean,
      default: undefined
    },
    allowSlidePrev: {
      type: Boolean,
      default: undefined
    },
    allowSlideNext: {
      type: Boolean,
      default: undefined
    },
    swipeHandler: {
      type: Boolean,
      default: undefined
    },
    noSwiping: {
      type: Boolean,
      default: undefined
    },
    noSwipingClass: {
      type: String,
      default: undefined
    },
    noSwipingSelector: {
      type: String,
      default: undefined
    },
    passiveListeners: {
      type: Boolean,
      default: undefined
    },
    containerModifierClass: {
      type: String,
      default: undefined
    },
    slideClass: {
      type: String,
      default: undefined
    },
    slideBlankClass: {
      type: String,
      default: undefined
    },
    slideActiveClass: {
      type: String,
      default: undefined
    },
    slideDuplicateActiveClass: {
      type: String,
      default: undefined
    },
    slideVisibleClass: {
      type: String,
      default: undefined
    },
    slideDuplicateClass: {
      type: String,
      default: undefined
    },
    slideNextClass: {
      type: String,
      default: undefined
    },
    slideDuplicateNextClass: {
      type: String,
      default: undefined
    },
    slidePrevClass: {
      type: String,
      default: undefined
    },
    slideDuplicatePrevClass: {
      type: String,
      default: undefined
    },
    wrapperClass: {
      type: String,
      default: undefined
    },
    runCallbacksOnInit: {
      type: Boolean,
      default: undefined
    },
    observer: {
      type: Boolean,
      default: undefined
    },
    observeParents: {
      type: Boolean,
      default: undefined
    },
    observeSlideChildren: {
      type: Boolean,
      default: undefined
    },
    a11y: {
      type: [Boolean, Object],
      default: undefined
    },
    autoplay: {
      type: [Boolean, Object],
      default: undefined
    },
    controller: {
      type: Object,
      default: undefined
    },
    coverflowEffect: {
      type: Object,
      default: undefined
    },
    cubeEffect: {
      type: Object,
      default: undefined
    },
    fadeEffect: {
      type: Object,
      default: undefined
    },
    flipEffect: {
      type: Object,
      default: undefined
    },
    creativeEffect: {
      type: Object,
      default: undefined
    },
    cardsEffect: {
      type: Object,
      default: undefined
    },
    hashNavigation: {
      type: [Boolean, Object],
      default: undefined
    },
    history: {
      type: [Boolean, Object],
      default: undefined
    },
    keyboard: {
      type: [Boolean, Object],
      default: undefined
    },
    lazy: {
      type: [Boolean, Object],
      default: undefined
    },
    mousewheel: {
      type: [Boolean, Object],
      default: undefined
    },
    navigation: {
      type: [Boolean, Object],
      default: undefined
    },
    pagination: {
      type: [Boolean, Object],
      default: undefined
    },
    parallax: {
      type: [Boolean, Object],
      default: undefined
    },
    scrollbar: {
      type: [Boolean, Object],
      default: undefined
    },
    thumbs: {
      type: Object,
      default: undefined
    },
    virtual: {
      type: [Boolean, Object],
      default: undefined
    },
    zoom: {
      type: [Boolean, Object],
      default: undefined
    },
    grid: {
      type: [Object],
      default: undefined
    },
    freeMode: {
      type: [Boolean, Object],
      default: undefined
    },
    enabled: {
      type: Boolean,
      default: undefined
    }
  },
  emits: ['_beforeBreakpoint', '_containerClasses', '_slideClass', '_slideClasses', '_swiper', '_freeModeNoMomentumRelease', 'activeIndexChange', 'afterInit', 'autoplay', 'autoplayStart', 'autoplayStop', 'autoplayPause', 'autoplayResume', 'beforeDestroy', 'beforeInit', 'beforeLoopFix', 'beforeResize', 'beforeSlideChangeStart', 'beforeTransitionStart', 'breakpoint', 'changeDirection', 'click', 'disable', 'doubleTap', 'doubleClick', 'destroy', 'enable', 'fromEdge', 'hashChange', 'hashSet', 'imagesReady', 'init', 'keyPress', 'lazyImageLoad', 'lazyImageReady', 'lock', 'loopFix', 'momentumBounce', 'navigationHide', 'navigationShow', 'navigationPrev', 'navigationNext', 'observerUpdate', 'orientationchange', 'paginationHide', 'paginationRender', 'paginationShow', 'paginationUpdate', 'progress', 'reachBeginning', 'reachEnd', 'realIndexChange', 'resize', 'scroll', 'scrollbarDragEnd', 'scrollbarDragMove', 'scrollbarDragStart', 'setTransition', 'setTranslate', 'slideChange', 'slideChangeTransitionEnd', 'slideChangeTransitionStart', 'slideNextTransitionEnd', 'slideNextTransitionStart', 'slidePrevTransitionEnd', 'slidePrevTransitionStart', 'slideResetTransitionStart', 'slideResetTransitionEnd', 'sliderMove', 'sliderFirstMove', 'slidesLengthChange', 'slidesGridLengthChange', 'snapGridLengthChange', 'snapIndexChange', 'swiper', 'tap', 'toEdge', 'touchEnd', 'touchMove', 'touchMoveOpposite', 'touchStart', 'transitionEnd', 'transitionStart', 'unlock', 'update', 'virtualUpdate', 'zoomChange'],

  setup(props, _ref) {
    let {
      slots: originalSlots,
      emit
    } = _ref;
    const {
      tag: Tag,
      wrapperTag: WrapperTag
    } = props;
    const containerClasses = ref('swiper');
    const virtualData = ref(null);
    const breakpointChanged = ref(false);
    const initializedRef = ref(false);
    const swiperElRef = ref(null);
    const swiperRef = ref(null);
    const oldPassedParamsRef = ref(null);
    const slidesRef = {
      value: []
    };
    const oldSlidesRef = {
      value: []
    };
    const nextElRef = ref(null);
    const prevElRef = ref(null);
    const paginationElRef = ref(null);
    const scrollbarElRef = ref(null);
    const {
      params: swiperParams,
      passedParams
    } = getParams(props, false);
    getChildren(originalSlots, slidesRef, oldSlidesRef);
    oldPassedParamsRef.value = passedParams;
    oldSlidesRef.value = slidesRef.value;

    const onBeforeBreakpoint = () => {
      getChildren(originalSlots, slidesRef, oldSlidesRef);
      breakpointChanged.value = true;
    };

    swiperParams.onAny = function (event) {
      for (var _len = arguments.length, args = new Array(_len > 1 ? _len - 1 : 0), _key = 1; _key < _len; _key++) {
        args[_key - 1] = arguments[_key];
      }

      emit(event, ...args);
    };

    Object.assign(swiperParams.on, {
      _beforeBreakpoint: onBeforeBreakpoint,

      _containerClasses(swiper, classes) {
        containerClasses.value = classes;
      }

    }); // init Swiper

    swiperRef.value = new SwiperCore(swiperParams);

    swiperRef.value.loopCreate = () => {};

    swiperRef.value.loopDestroy = () => {};

    if (swiperParams.loop) {
      swiperRef.value.loopedSlides = calcLoopedSlides(slidesRef.value, swiperParams);
    }

    if (swiperRef.value.virtual && swiperRef.value.params.virtual.enabled) {
      swiperRef.value.virtual.slides = slidesRef.value;
      const extendWith = {
        cache: false,
        slides: slidesRef.value,
        renderExternal: data => {
          virtualData.value = data;
        },
        renderExternalUpdate: false
      };
      extend(swiperRef.value.params.virtual, extendWith);
      extend(swiperRef.value.originalParams.virtual, extendWith);
    }

    onUpdated(() => {
      // set initialized flag
      if (!initializedRef.value && swiperRef.value) {
        swiperRef.value.emitSlidesClasses();
        initializedRef.value = true;
      } // watch for params change


      const {
        passedParams: newPassedParams
      } = getParams(props, false);
      const changedParams = getChangedParams(newPassedParams, oldPassedParamsRef.value, slidesRef.value, oldSlidesRef.value, c => c.props && c.props.key);
      oldPassedParamsRef.value = newPassedParams;

      if ((changedParams.length || breakpointChanged.value) && swiperRef.value && !swiperRef.value.destroyed) {
        updateSwiper({
          swiper: swiperRef.value,
          slides: slidesRef.value,
          passedParams: newPassedParams,
          changedParams,
          nextEl: nextElRef.value,
          prevEl: prevElRef.value,
          scrollbarEl: scrollbarElRef.value,
          paginationEl: paginationElRef.value
        });
      }

      breakpointChanged.value = false;
    });
    provide('swiper', swiperRef); // update on virtual update

    watch(virtualData, () => {
      nextTick(() => {
        updateOnVirtualData(swiperRef.value);
      });
    }); // mount swiper

    onMounted(() => {
      if (!swiperElRef.value) return;
      mountSwiper({
        el: swiperElRef.value,
        nextEl: nextElRef.value,
        prevEl: prevElRef.value,
        paginationEl: paginationElRef.value,
        scrollbarEl: scrollbarElRef.value,
        swiper: swiperRef.value
      }, swiperParams);
      emit('swiper', swiperRef.value);
    });
    onBeforeUnmount(() => {
      if (swiperRef.value && !swiperRef.value.destroyed) {
        swiperRef.value.destroy(true, false);
      }
    }); // bypass swiper instance to slides

    function renderSlides(slides) {
      if (swiperParams.virtual) {
        return renderVirtual(swiperRef, slides, virtualData.value);
      }

      if (!swiperParams.loop || swiperRef.value && swiperRef.value.destroyed) {
        slides.forEach(slide => {
          if (!slide.props) slide.props = {};
          slide.props.swiperRef = swiperRef;
        });
        return slides;
      }

      return renderLoop(swiperRef, slides, swiperParams);
    }

    return () => {
      const {
        slides,
        slots
      } = getChildren(originalSlots, slidesRef, oldSlidesRef);
      return h(Tag, {
        ref: swiperElRef,
        class: uniqueClasses(containerClasses.value)
      }, [slots['container-start'], h(WrapperTag, {
        class: 'swiper-wrapper'
      }, [slots['wrapper-start'], renderSlides(slides), slots['wrapper-end']]), needsNavigation(props) && [h('div', {
        ref: prevElRef,
        class: 'swiper-button-prev'
      }), h('div', {
        ref: nextElRef,
        class: 'swiper-button-next'
      })], needsScrollbar(props) && h('div', {
        ref: scrollbarElRef,
        class: 'swiper-scrollbar'
      }), needsPagination(props) && h('div', {
        ref: paginationElRef,
        class: 'swiper-pagination'
      }), slots['container-end']]);
    };
  }

};
export { Swiper };