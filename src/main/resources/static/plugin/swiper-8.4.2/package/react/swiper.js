function _extends() { _extends = Object.assign ? Object.assign.bind() : function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; }; return _extends.apply(this, arguments);
}

import React, {forwardRef, useEffect, useRef, useState} from 'react';
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
import {useIsomorphicLayoutEffect} from './use-isomorphic-layout-effect.js';
import {SwiperContext} from './context.js';

const Swiper = /*#__PURE__*/forwardRef(function (_temp, externalElRef) {
  let {
    className,
    tag: Tag = 'div',
    wrapperTag: WrapperTag = 'div',
    children,
    onSwiper,
    ...rest
  } = _temp === void 0 ? {} : _temp;
  let eventsAssigned = false;
  const [containerClasses, setContainerClasses] = useState('swiper');
  const [virtualData, setVirtualData] = useState(null);
  const [breakpointChanged, setBreakpointChanged] = useState(false);
  const initializedRef = useRef(false);
  const swiperElRef = useRef(null);
  const swiperRef = useRef(null);
  const oldPassedParamsRef = useRef(null);
  const oldSlides = useRef(null);
  const nextElRef = useRef(null);
  const prevElRef = useRef(null);
  const paginationElRef = useRef(null);
  const scrollbarElRef = useRef(null);
  const {
    params: swiperParams,
    passedParams,
    rest: restProps,
    events
  } = getParams(rest);
  const {
    slides,
    slots
  } = getChildren(children);

  const onBeforeBreakpoint = () => {
    setBreakpointChanged(!breakpointChanged);
  };

  Object.assign(swiperParams.on, {
    _containerClasses(swiper, classes) {
      setContainerClasses(classes);
    }

  });

  const initSwiper = () => {
    // init swiper
    Object.assign(swiperParams.on, events);
    eventsAssigned = true;
    swiperRef.current = new SwiperCore(swiperParams);

    swiperRef.current.loopCreate = () => {};

    swiperRef.current.loopDestroy = () => {};

    if (swiperParams.loop) {
      swiperRef.current.loopedSlides = calcLoopedSlides(slides, swiperParams);
    }

    if (swiperRef.current.virtual && swiperRef.current.params.virtual.enabled) {
      swiperRef.current.virtual.slides = slides;
      const extendWith = {
        cache: false,
        slides,
        renderExternal: setVirtualData,
        renderExternalUpdate: false
      };
      extend(swiperRef.current.params.virtual, extendWith);
      extend(swiperRef.current.originalParams.virtual, extendWith);
    }
  };

  if (!swiperElRef.current) {
    initSwiper();
  } // Listen for breakpoints change


  if (swiperRef.current) {
    swiperRef.current.on('_beforeBreakpoint', onBeforeBreakpoint);
  }

  const attachEvents = () => {
    if (eventsAssigned || !events || !swiperRef.current) return;
    Object.keys(events).forEach(eventName => {
      swiperRef.current.on(eventName, events[eventName]);
    });
  };

  const detachEvents = () => {
    if (!events || !swiperRef.current) return;
    Object.keys(events).forEach(eventName => {
      swiperRef.current.off(eventName, events[eventName]);
    });
  };

  useEffect(() => {
    return () => {
      if (swiperRef.current) swiperRef.current.off('_beforeBreakpoint', onBeforeBreakpoint);
    };
  }); // set initialized flag

  useEffect(() => {
    if (!initializedRef.current && swiperRef.current) {
      swiperRef.current.emitSlidesClasses();
      initializedRef.current = true;
    }
  }); // mount swiper

  useIsomorphicLayoutEffect(() => {
    if (externalElRef) {
      externalElRef.current = swiperElRef.current;
    }

    if (!swiperElRef.current) return;

    if (swiperRef.current.destroyed) {
      initSwiper();
    }

    mountSwiper({
      el: swiperElRef.current,
      nextEl: nextElRef.current,
      prevEl: prevElRef.current,
      paginationEl: paginationElRef.current,
      scrollbarEl: scrollbarElRef.current,
      swiper: swiperRef.current
    }, swiperParams);
    if (onSwiper) onSwiper(swiperRef.current); // eslint-disable-next-line

    return () => {
      if (swiperRef.current && !swiperRef.current.destroyed) {
        swiperRef.current.destroy(true, false);
      }
    };
  }, []); // watch for params change

  useIsomorphicLayoutEffect(() => {
    attachEvents();
    const changedParams = getChangedParams(passedParams, oldPassedParamsRef.current, slides, oldSlides.current, c => c.key);
    oldPassedParamsRef.current = passedParams;
    oldSlides.current = slides;

    if (changedParams.length && swiperRef.current && !swiperRef.current.destroyed) {
      updateSwiper({
        swiper: swiperRef.current,
        slides,
        passedParams,
        changedParams,
        nextEl: nextElRef.current,
        prevEl: prevElRef.current,
        scrollbarEl: scrollbarElRef.current,
        paginationEl: paginationElRef.current
      });
    }

    return () => {
      detachEvents();
    };
  }); // update on virtual update

  useIsomorphicLayoutEffect(() => {
    updateOnVirtualData(swiperRef.current);
  }, [virtualData]); // bypass swiper instance to slides

  function renderSlides() {
    if (swiperParams.virtual) {
      return renderVirtual(swiperRef.current, slides, virtualData);
    }

    if (!swiperParams.loop || swiperRef.current && swiperRef.current.destroyed) {
      return slides.map(child => {
        return /*#__PURE__*/React.cloneElement(child, {
          swiper: swiperRef.current
        });
      });
    }

    return renderLoop(swiperRef.current, slides, swiperParams);
  }

  return /*#__PURE__*/React.createElement(Tag, _extends({
    ref: swiperElRef,
    className: uniqueClasses(`${containerClasses}${className ? ` ${className}` : ''}`)
  }, restProps), /*#__PURE__*/React.createElement(SwiperContext.Provider, {
    value: swiperRef.current
  }, slots['container-start'], /*#__PURE__*/React.createElement(WrapperTag, {
    className: "swiper-wrapper"
  }, slots['wrapper-start'], renderSlides(), slots['wrapper-end']), needsNavigation(swiperParams) && /*#__PURE__*/React.createElement(React.Fragment, null, /*#__PURE__*/React.createElement("div", {
    ref: prevElRef,
    className: "swiper-button-prev"
  }), /*#__PURE__*/React.createElement("div", {
    ref: nextElRef,
    className: "swiper-button-next"
  })), needsScrollbar(swiperParams) && /*#__PURE__*/React.createElement("div", {
    ref: scrollbarElRef,
    className: "swiper-scrollbar"
  }), needsPagination(swiperParams) && /*#__PURE__*/React.createElement("div", {
    ref: paginationElRef,
    className: "swiper-pagination"
  }), slots['container-end']));
});
Swiper.displayName = 'Swiper';
export { Swiper };