/* SmartWizard v4.0.5
 * jQuery Wizard Plugin
 * http://www.techlaboratory.net/smartwizard
 * 
 * Created by Dipu Raj  
 * http://dipuraj.me
 * 
 * Licensed under the terms of the MIT License
 * https://github.com/techlab/SmartWizard/blob/master/MIT-LICENSE.txt 
 */

(function(d, c, a, f) {
	var e = {
		selected : 0,
		keyNavigation : true,
		autoAdjustHeight : true,
		cycleSteps : false,
		backButtonSupport : true,
		useURLhash : true,
		lang : {
			next : "Next",
			previous : "Previous"
		},
		toolbarSettings : {
			toolbarPosition : "bottom",
			toolbarButtonPosition : "right",
			showNextButton : true,
			showPreviousButton : true,
			toolbarExtraButtons : []
		},
		anchorSettings : {
			anchorClickable : true,
			enableAllAnchors : false,
			markDoneStep : true,
			enableAnchorOnDoneStep : true
		},
		contentURL : null,
		disabledSteps : [],
		errorSteps : [],
		theme : "default",
		transitionEffect : "none",
		transitionSpeed : "400"
	};
	function b(h, g) {
		this._defaults = e;
		this.options = d.extend(true, {}, e, g);
		this.main = d(h);
		this.nav = this.main.children("ul");
		this.steps = d("li > a", this.nav);
		this.container = this.main.children("div");
		this.pages = this.container.children("div");
		this.current_index = null;
		this.is_animating = false;
		this.init()
	}
	d.extend(b.prototype,{
						init : function() {
							var g = this.options.selected;
							if (this.options.useURLhash) {
								var h = c.location.hash;
								if (h && h.length > 0) {
									var j = d("a[href*=" + h + "]");
									if (j.length > 0) {
										var i = this.steps.index(j);
										g = (i >= 0) ? i : g
									}
								}
							}
							this._setElements();
							this._setToolbar();
							this._setEvents();
							this._showStep(g)
						},
						_setElements : function() {
							this.main.addClass("sw-main sw-theme-"
									+ this.options.theme);
							this.nav.addClass("nav nav-tabs step-anchor");
							if (this.options.anchorSettings.enableAllAnchors !== false
									&& this.options.anchorSettings.anchorClickable !== false) {
								this.steps.parent("li").addClass("clickable")
							}
							this.container.addClass("sw-container tab-content");
							this.pages.addClass("step-content");
							var g = this;
							if (this.options.disabledSteps
									&& this.options.disabledSteps.length > 0) {
								d.each(this.options.disabledSteps, function(h,
										j) {
									g.steps.eq(j).parent("li").addClass(
											"disabled")
								})
							}
							if (this.options.errorSteps
									&& this.options.errorSteps.length > 0) {
								d.each(this.options.errorSteps, function(h, j) {
									g.steps.eq(j).parent("li").addClass(
											"danger")
								})
							}
							return true
						},
						_setToolbar : function() {
							return true;
							if (this.options.toolbarSettings.toolbarPosition === "none") {
								return true
							}
							var i = (this.options.toolbarSettings.showNextButton !== false) ? d(
									"<button></button>").text(
									this.options.lang.next).addClass(
									"btn btn-default sw-btn-next").attr("type",
									"button")
									: null;
							var h = (this.options.toolbarSettings.showPreviousButton !== false) ? d(
									"<button></button>").text(
									this.options.lang.previous).addClass(
									"btn btn-default sw-btn-prev").attr("type",
									"button")
									: null;
							var l = d("<div></div>")
									.addClass(
											"btn-group navbar-btn sw-btn-group pull-"
													+ this.options.toolbarSettings.toolbarButtonPosition)
									.attr("role", "group").append(h, i);
							var k = null;
							if (this.options.toolbarSettings.toolbarExtraButtons
									&& this.options.toolbarSettings.toolbarExtraButtons.length > 0) {
								k = d("<div></div>")
										.addClass(
												"btn-group navbar-btn sw-btn-group-extra pull-"
														+ this.options.toolbarSettings.toolbarButtonPosition)
										.attr("role", "group");
								d
										.each(
												this.options.toolbarSettings.toolbarExtraButtons,
												function(m, o) {
													o.css = (o.css && o.css.length > 0) ? o.css
															: "btn-default";
													k
															.append(d(
																	"<button></button>")
																	.text(
																			o.label)
																	.addClass(
																			"btn "
																					+ o.css)
																	.attr(
																			"type",
																			"button")
																	.on(
																			"click",
																			function() {
																				o.onClick
																						.call(this)
																			}))
												})
							}
							switch (this.options.toolbarSettings.toolbarPosition) {
							case "top":
								var j = d("<nav></nav>")
										.addClass(
												"navbar btn-toolbar sw-toolbar sw-toolbar-top");
								j.append(l);
								if (this.options.toolbarSettings.toolbarButtonPosition === "left") {
									j.append(k)
								} else {
									j.prepend(k)
								}
								this.container.before(j);
								break;
							case "bottom":
								var g = d("<nav></nav>")
										.addClass(
												"navbar btn-toolbar sw-toolbar sw-toolbar-bottom");
								g.append(l);
								if (this.options.toolbarSettings.toolbarButtonPosition === "left") {
									g.append(k)
								} else {
									g.prepend(k)
								}
								this.container.after(g);
								break;
							case "both":
								var j = d("<nav></nav>")
										.addClass(
												"navbar btn-toolbar sw-toolbar sw-toolbar-top");
								j.append(l);
								if (this.options.toolbarSettings.toolbarButtonPosition === "left") {
									j.append(k)
								} else {
									j.prepend(k)
								}
								this.container.before(j);
								var g = d("<nav></nav>")
										.addClass(
												"navbar btn-toolbar sw-toolbar sw-toolbar-bottom");
								g.append(l.clone(true));
								if (this.options.toolbarSettings.toolbarButtonPosition === "left") {
									g.append(k.clone(true))
								} else {
									g.prepend(k.clone(true))
								}
								this.container.after(g);
								break;
							default:
								var g = d("<nav></nav>")
										.addClass(
												"navbar btn-toolbar sw-toolbar sw-toolbar-bottom");
								g.append(l);
								if (this.options.toolbarSettings.toolbarButtonPosition === "left") {
									g.append(k)
								} else {
									g.prepend(k)
								}
								this.container.after(g);
								break
							}
							return true
						},
						_setEvents : function() {
							var g = this;
							d(this.steps)
									.on("click",
											function(i) {
												i.preventDefault();
												if (g.options.anchorSettings.anchorClickable === false) {
													return true
												}
												var h = g.steps.index(this);
												if (g.options.anchorSettings.enableAnchorOnDoneStep === false
														&& g.steps.eq(h)
																.parent("li")
																.hasClass(
																		"done")) {
													return true
												}
												if (h !== g.current_index) {
													if (g.options.anchorSettings.enableAllAnchors !== false
															&& g.options.anchorSettings.anchorClickable !== false) {
														g._showStep(h)
													} else {
														if (g.steps.eq(h)
																.parent("li")
																.hasClass(
																		"done")) {
															g._showStep(h)
														}
													}
												}
											});
							d(".sw-btn-next", this.main)
									.on(
											"click",
											function(h) {
												h.preventDefault();
												if (g.steps.index(this) !== g.current_index) {
													g._showNext()
												}
											});
							d(".sw-btn-prev", this.main)
									.on(
											"click",
											function(h) {
												h.preventDefault();
												if (g.steps.index(this) !== g.current_index) {
													g._showPrevious()
												}
											});
							if (this.options.keyNavigation) {
								d(a).keyup(function(h) {
									g._keyNav(h)
								})
							}
							if (this.options.backButtonSupport) {
								d(c)
										.on(
												"hashchange",
												function() {
													if (!g.options.useURLhash) {
														return true
													}
													if (c.location.hash) {
														var h = d("a[href*="
																+ c.location.hash
																+ "]");
														if (h && h.length > 0) {
															g._showStep(g.steps
																	.index(h))
														}
													}
												})
							}
							return true
						},
						_showNext : function() {
							var g = this.current_index + 1;
							for (var h = g; h < this.steps.length; h++) {
								if (!this.steps.eq(h).parent("li").hasClass(
										"disabled")) {
									g = h;
									break
								}
							}
							if (this.steps.length <= g) {
								if (!this.options.cycleSteps) {
									return false
								}
								g = 0
							}
							this._showStep(g);
							
							return true
						},
						_showPrevious : function() {
							var g = this.current_index - 1;							
							for (var h = g; h >= 0; h--) {
								if (!this.steps.eq(h).parent("li").hasClass(
										"disabled")) {
									g = h;
									break
								}
							}
							if (0 > g) {
								if (!this.options.cycleSteps) {
									return false
								}
								g = this.steps.length - 1
							}
							this._showStep(g);
							return true
						},
						_showStep : function(g) {
							if (!this.steps.eq(g)) {
								return false
							}
							if (g == this.current_index) {
								return false
							}
							if (this.steps.eq(g).parent("li").hasClass(
									"disabled")) {
								return false
							}
							this._loadStepContent(g);
							cp(g);							
							return true
						},
						_loadStepContent : function(g) {
							var h = this;
							var k = this.steps.eq(g);
							var j = (k.data("content-url") && k
									.data("content-url").length > 0) ? k
									.data("content-url")
									: this.options.contentURL;
							if (j && j.length > 0 && !k.data("has-content")) {
								var i = (k.length > 0) ? d(k.attr("href"),
										this.main) : null;
								d.ajax({
									url : j,
									type : "POST",
									data : ({
										step_number : g
									}),
									dataType : "text",
									beforeSend : function() {
										k.parent("li").addClass("loading")
									},
									error : function() {
										k.parent("li").removeClass("loading")
									},
									success : function(l) {
										if (l && l.length > 0) {
											k.data("has-content", true);
											i.html(l)
										}
										k.parent("li").removeClass("loading");
										h._transitPage(g)
									}
								})
							} else {
								this._transitPage(g)
							}
							return true
						},
						_transitPage : function(h) {
							var i = this;
							if (this.is_animating) {
								return false
							}
							var g = this.steps.eq(this.current_index);
							var l = (g.length > 0) ? d(g.attr("href"),
									this.main) : null;
							var k = this.steps.eq(h);
							var m = (k.length > 0) ? d(k.attr("href"),
									this.main) : null;
							var j = "";
							if (this.current_index !== null
									&& this.current_index !== h) {
								j = (this.current_index < h) ? "forward"
										: "backward"
							}
							if (this.current_index !== null
									&& this._triggerEvent("leaveStep", [ g,
											this.current_index, j ]) === false) {
								return false
							}
							this.is_animating = true;
							this.options.transitionEffect = this.options.transitionEffect
									.toLowerCase();
							this.pages.finish();
							if (this.options.transitionEffect === "slide") {
								if (l && l.length > 0) {
									l
											.slideUp(
													"fast",
													this.options.transitionEasing,
													function() {
														m
																.slideDown(
																		i.options.transitionSpeed,
																		i.options.transitionEasing)
													})
								} else {
									m.slideDown(this.options.transitionSpeed,
											this.options.transitionEasing)
								}
							} else {
								if (this.options.transitionEffect === "fade") {
									if (l && l.length > 0) {
										l
												.fadeOut(
														"fast",
														this.options.transitionEasing,
														function() {
															m
																	.fadeIn(
																			"fast",
																			i.options.transitionEasing,
																			function() {
																				d(
																						this)
																						.show()
																			})
														})
									} else {
										m.fadeIn(this.options.transitionSpeed,
												this.options.transitionEasing,
												function() {
													d(this).show()
												})
									}
								} else {
									if (l && l.length > 0) {
										l.hide()
									}
									m.show()
								}
							}
							c.location.hash = k.attr("href");
							this._setAnchor(h);
							this._setButtons(h);
							this._fixHeight(h);
							this.current_index = h;
							this.is_animating = false;
							this._triggerEvent("showStep", [ k,
									this.current_index, j ]);
							return true
						},
						_setAnchor : function(g) {
							this.steps.eq(this.current_index).parent("li")
									.removeClass("active danger loading");
							if (this.options.anchorSettings.markDoneStep !== false
									&& this.current_index !== null) {
								this.steps.eq(this.current_index).parent("li")
										.addClass("done")
							}
							this.steps.eq(g).parent("li").removeClass(
									"done danger loading").addClass("active");
							return true
						},
						_setButtons : function(g) {
							if (!this.options.cycleSteps) {
								if (0 >= g) {
									d(".sw-btn-prev", this.main).addClass(
											"disabled")
								} else {
									d(".sw-btn-prev", this.main).removeClass(
											"disabled")
								}
								if ((this.steps.length - 1) <= g) {
									d(".sw-btn-next", this.main).addClass(
											"disabled")
								} else {
									d(".sw-btn-next", this.main).removeClass(
											"disabled")
								}
							}
							return true
						},
						_keyNav : function(h) {
							var g = this;
							switch (h.which) {
							case 37:
								g._showPrevious();
								h.preventDefault();
								break;
							case 39:
								g._showNext();
								h.preventDefault();
								break;
							default:
								return
							}
						},
						_fixHeight : function(g) {
							if (this.options.autoAdjustHeight) {
								var h = (this.steps.eq(g).length > 0) ? d(
										this.steps.eq(g).attr("href"),
										this.main) : null;
								this.container.finish().animate({
									height : h.outerHeight()
								}, this.options.transitionSpeed, function() {
								})
							}
							return true
						},
						_triggerEvent : function(g, i) {
							var h = d.Event(g);
							this.main.trigger(h, i);
							if (h.isDefaultPrevented()) {
								return false
							}
							return h.result
						},
						theme : function(g) {
							if (this.options.theme === g) {
								return false
							}
							this.main.removeClass("sw-theme-"
									+ this.options.theme);
							this.options.theme = g;
							this.main
									.addClass("sw-theme-" + this.options.theme);
							this._triggerEvent("themeChanged",
									[ this.options.theme ])
						},
						next : function() {
							this._showNext()
						},
						prev : function() {
							this._showPrevious()
						},
						reset : function() {
							if (this._triggerEvent("beginReset") === false) {
								return false
							}
							this.container.stop(true);
							this.pages.stop(true);
							this.pages.hide();
							this.current_index = null;
							c.location.hash = this.steps.eq(
									this.options.selected).attr("href");
							d(".sw-toolbar", this.main).remove();
							this.steps.removeClass();
							this.steps.parents("li").removeClass();
							this.steps.data("has-content", false);
							this.init();
							this._triggerEvent("endReset")
						}
					});
	d.fn.smartWizard = function(i) {
		var h = arguments;
		var g;
		if (i === f || typeof i === "object") {
			return this.each(function() {
				if (!d.data(this, "smartWizard")) {
					d.data(this, "smartWizard", new b(this, i))
				}
			})
		} else {
			if (typeof i === "string" && i[0] !== "_" && i !== "init") {
				g = d.data(this[0], "smartWizard");
				if (i === "destroy") {
					d.data(this, "smartWizard", null)
				}
				if (g instanceof b && typeof g[i] === "function") {
					return g[i].apply(g, Array.prototype.slice.call(h, 1))
				} else {
					return this
				}
			}
		}		
	}
})(jQuery, window, document);
$("body").show("slow");