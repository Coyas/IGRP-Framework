<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <!-- HEAD -->
  <xsl:template name="IGRP-head">
   
     <title><xsl:value-of select="rows/title"/></title>
    <link REL="SHORTCUT ICON" HREF="{$path}/assets/img/favicon.ico" />

    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>

    <link rel="mask-icon" href="{$path}/themes/default/img/logo2.svg" color="#5bbad5"/>
    
    <!-- Normalize -->
    <link rel="stylesheet" href="{$path}/core/normalize/normalize.css" media="none" onload="if(media!='all')media='all'"/>
    <!-- /Normalize -->

    <!-- Bootstrap Include CSS -->
    <xsl:if test="not($themeConfigData/css/@bootstrap) or $themeConfigData/css/@bootstrap!='false'">
      <link rel="stylesheet" href="{$path}/core/bootstrap/{$bs-v}/css/bootstrap.min.css" media="none" onload="if(media!='all')media='all'"/>
      <link rel="stylesheet" href="{$path}/themes/bs.columns.css" media="none" onload="if(media!='all')media='all'"/>
      <link rel="stylesheet" href="{$path}/themes/bs.class.css" media="none" onload="if(media!='all')media='all'"/>
    </xsl:if> 
    <xsl:if test="$themeConfigData/@version = '4'">
      <link href="{$path}/core/bootstrap/bootstrap-iso.css?v={$version}" rel="stylesheet"/>
    </xsl:if>
    <!-- /Bootstrap Include CSS -->

     <!-- FontAwesome -->
    <link rel="stylesheet" href="{$path}/core/fontawesome/4.7/css/font-awesome.min.css"/>
    <!-- /FontAwesome -->

    <!-- COLOR PALETTES -->
    <link rel="stylesheet" type="text/css" href="{$path}/core/colorpalettes/old-palettes.css"  media="none" onload="if(media!='all')media='all'"  />
    <xsl:call-template name="colorpalettes-css"/>    
    <!-- /COLOR PALETTES -->

	  <!--right panel-->
    <link rel="stylesheet" href="{$path}/core/igrp/rightpanel/rightpanel.css" media="none" onload="if(media!='all')media='all'"/>
    <!--/right panel-->

    <!--roboto font-->
   	<link href="{$path}/themes/robotofont.css" rel='stylesheet' type='text/css'  media="none" onload="if(media!='all')media='all'" />
    <!--/roboto font-->

    <!-- allways include forms.css -->
    <link rel="stylesheet" type="text/css" href="{$path}/core/igrp/form/igrp.forms.css" media="none" onload="if(media!='all')media='all'" />	  
    <!-- /allways include forms.css -->

    <!-- DEFAULT CSS -->
    <xsl:choose>
      <xsl:when test="not($themeConfigData/css/@default) or $themeConfigData/css/@default != 'false'">
        <link rel="stylesheet" href="{$path}/themes/style.css" />
      </xsl:when>
      <xsl:when test="$themeConfigData/css/@custom">
        <link rel="stylesheet" href="{$themePath}/{$themeConfigData/css/@custom}" />
      </xsl:when>
    </xsl:choose>
    <!-- /DEFAULT CSS-->
    
    <!-- THEME CSS -->
    <xsl:for-each select="$themeConfigData/css/file">
      <xsl:choose>
        <xsl:when test="@external = 'true'">
          <link href="{.}" rel="stylesheet"/>
        </xsl:when>
        <xsl:otherwise>
          <link href="{$themePath}/{.}" rel="stylesheet" media="none" onload="if(media!='all')media='all'"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:for-each>
    <!-- /THEME CSS -->

    <!--theme configuration generator-->
    <xsl:if test="$themeConfigData">
      <xsl:apply-templates mode="theme-colors-config" select="$themeConfigData"/>
    </xsl:if>
   <!--/theme configuration generator-->

    <style>
      .grecaptcha-badge{display:none!important;}
    </style>
    <!--/DEFAULT CSS --> 
    
    <script>
   		var path        = '<xsl:value-of select="$path"/>';
   		var ispublic    = '<xsl:value-of select="$ispublic"/>';
      	var rekey       = '<xsl:value-of select="$recaptchakey"/>'; 
      	var secretrekey = '<xsl:value-of select="$secretrecaptchakey"/>'; 
	</script>
	  <xsl:if test="$ispublic = 1">
      	<!-- reCAPTCHA -->
      	<script src="https://www.google.com/recaptcha/api.js?render={$recaptchakey}"></script>
   	 </xsl:if>

    <!-- BS CSS -->    
   <!-- NOT ASYNC JS-->   
    <script src="{$path}/core/promise/promise.min.js" ></script>    
    <script src="{$path}/core/jquery/{$jq-v}/jquery.min.js" ></script>  
<!--    <script src="{$path}/core/jquery/2.1/jquery-migrate.min.js" ></script>     -->
    <script src="{$path}/core/bootstrap/plugins/toolkit/bootstrap-toolkit.min.js"></script>  
    <!-- VERIFIED /NOT ASYNC JS-->  
 	  <script src="{$path}/core/bootstrap/plugins/validation/js/jquery.validate.js" ></script>    
    <script src="{$path}/core/moment/moment.min.js" ></script>  
    <script src="{$path}/core/bootstrap/plugins/notify/bootstrap-notify.min.js" ></script>    
    
    <!-- EVENTS class -->
    <script src="{$path}/core/igrp/IGRP.events.class.js?v={$version}"></script>
    <!-- UTILS class -->
    <script src="{$path}/core/igrp/IGRP.jsutils.js?v={$version}" ></script>
    <!-- IGRP class -->
    <script src="{$path}/core/igrp/IGRP.class.js?v={$version}" ></script>
    <!-- IGRP config -->
    <script src="{$path}/core/igrp/IGRP.defaults.js?v={$version}" ></script>
    <!-- IGRP core functions -->
    <script src="{$path}/core/igrp/IGRP.core.js?v={$version}" ></script>
    <!-- IGRP globalModal
    not async -->
    <script src="{$path}/core/igrp/globalmodal/IGRP.globalModal.js?v={$version}"  ></script>
    <!-- IGRP iframeNavigation 
       not async -->
    <script src="{$path}/core/igrp/iframenav/IGRP.iframeNav.js?v={$version}"  ></script>
    <!-- IGRP targets controller -->
    <script src="{$path}/core/igrp/targets/IGRP.targets.js?v={$version}" ></script>
    <!-- IGRP targets controller -->
    <script src="{$path}/core/igrp/tree/IGRP.tree.js?v={$version}" ></script>
    <!-- IGRP sidebar controller -->
    <script src="{$path}/core/igrp/sidebar/IGRP.sidebar.js?v={$version}"></script>
    <!-- IGRP list association controller -->
    <script src="{$path}/core/igrp/components/list-association/igrp.list.association.js?v={$version}"></script>
    <!-- IGRP searchlist controller -->
    <script src="{$path}/core/igrp/searchlist/IGRP.searchlist.js"  charset="UTF-8"></script>
    <!-- IGRP rightpanel controller -->
    <script src="{$path}/core/igrp/rightpanel/rightpanel.js" ></script>
    <!-- IGRP themes controller -->
    <script src="{$path}/core/igrp/themes/IGRP.themes.js?v={$version}" ></script>
    <!-- IGRP scroll to top controller -->
    <script src="{$path}/core/igrp/scrolltop/IGRP.scrolltop.js?v={$version}" ></script>
    <!-- IGRP XML XSL Transform -->
    <script src="{$path}/core/igrp/xml.xslt/xml.xsl.transform.js?v={$version}" ></script>
    <!-- IGRP handler -->
    <script encode="utf-8" src="{$path}/core/igrp/IGRP.handler.js?v={$version}" ></script>

    <xsl:call-template name="colorpalettes-js"/>

    <!-- THEME JS -->
    <xsl:for-each select="$themeConfigData/js/file">
      <script src="{$themePath}/{.}"></script>
    </xsl:for-each>
    <!-- /THEME JS -->

    <script>
      $(window).on('load', function(){
        $.IGRP.onLoad ? $.IGRP.onLoad() : null;
      });
      $(document).ready( function(){
      	$.IGRP.init( );
      });
    </script>

    
  </xsl:template>
  <!-- TOPMENU -->
  <xsl:template name="IGRP-topmenu">
    <xsl:attribute name="template"><xsl:value-of select="rows/template"/></xsl:attribute>
    <xsl:attribute name="has-menu"><xsl:value-of select="$hasMenu"/></xsl:attribute>
    <xsl:attribute name="page"><xsl:value-of select="rows/page"/></xsl:attribute>
    <xsl:attribute name="app"><xsl:value-of select="rows/app"/></xsl:attribute>
    <xsl:if test="$hasMenu = 'true'">
      <xsl:choose>
        <xsl:when test="$themeConfigData/@version = '4' ">
          <div class="" id="">
            <xsl:call-template name="header-v4"/>
          </div>
        </xsl:when>
        <xsl:otherwise>
<!--           <xsl:variable name="logo"> -->
<!--             <xsl:choose> -->
<!--               <xsl:when test="$themeConfigData/logo"> -->
<!--                 <xsl:value-of select="concat($themePath,'/',$themeConfigData/logo)"/> -->
<!--               </xsl:when> -->
<!--               <xsl:otherwise> -->
<!--                 <xsl:value-of select="concat($path,'/themes/default/img/logo2.svg')"/> -->
<!--               </xsl:otherwise> -->
<!--             </xsl:choose> -->
<!--           </xsl:variable> -->

          <nav id="igrp-top-nav" class="navbar navbar-fixed-top" bg-color="1">
            <a class="navbar-brand col-sm-3 col-md-2" href="{rows/link}" >
            <!-- <img src="{$logo}"/>
            <span class=""><b><xsl:value-of select="rows/title"/></b></span> -->
            </a>

            <div id="side-bar-ctrl">
               <img src="{$path}/assets/img/menu_icon.png"></img>
            </div>

            <div id="igrp-app-title" class="">
           		 <a style="color: inherit;" href="{rows/link}" >
            	<xsl:value-of select="rows/description"/>
            	 </a>
            </div>

            <xsl:variable name="topMenus" select="document(rows/top_menu/@file)/top_menu/button"/>

            <xsl:variable name="settingsURL" select="$topMenus[title = 'Settings']"/>

            <ul id="igrp-top-menu" class="nav navbar-nav navbar-right">
              
              <xsl:for-each select="$topMenus[not(title = 'Settings')]">
                
                <xsl:variable name="fa-icon">
                  <xsl:call-template name="topMenuIconsMap"/>
                </xsl:variable>

                <xsl:choose>
                  <xsl:when test="@type='mytask' or @type='availtask'">
                    <li alt="Tasks" title="Tasks">
                      <a href="{link}" target="{target}">
                        <i class="fa {$fa-icon}"></i>
                        <xsl:if test="title and title!=''">
                          <small class="badge" bg-color="2"><xsl:value-of select="title"/></small>
                        </xsl:if>
                      </a>
                    </li>
                  </xsl:when>
                  <xsl:otherwise>
                    <li alt="{title}" title="{title}">
                      <a href="{link}" target="{target}">
                        <i class="fa {$fa-icon}"></i>
                      </a>
                    </li>
                  </xsl:otherwise>
                </xsl:choose>

              </xsl:for-each>
              <li alt="{$settingsURL/title}" title="{$settingsURL/title}">       
                <a href="{$settingsURL/link}" target="{$settingsURL/target}" close="refresh">
                <xsl:if test="$settingsURL/img != ''">             
                  <img src="{$path}/assets/img/{$settingsURL/img}" style="width: 26px;margin-right: 4px;"></img>
                  </xsl:if>
                  <span class=""><b><xsl:value-of select="rows/site/user_name"/></b></span>         
                </a>
              </li>

              <li alt="{rows/site/button/title}" title="{rows/site/button/title}">
                <a href="{rows/site/button/link}" target="{rows/site/button/target}">
                  <i class="fa fa-sign-out"></i>
                </a>
              </li>

            </ul>

          </nav>
        </xsl:otherwise>
      </xsl:choose> 
      
    
    
    </xsl:if>
  </xsl:template>
  <!-- SIDERBAR -->
  <xsl:template name="IGRP-sidebar">
    <xsl:if test="$hasMenu = 'true'">
      <xsl:variable name="APP" select="rows/app" />
      <xsl:variable name="menus" select="document(rows/slide-menu/@file)/menus" />

      <xsl:choose>
        <xsl:when test="$themeConfigData/@version = '4' ">
          <xsl:call-template name="sidebar-v4"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:variable name="logo">
            <xsl:choose>
         	  <xsl:when test="$themeConfigData/logo">
             	<xsl:value-of select="concat($themePath,'/',$themeConfigData/logo)"/>
              </xsl:when>
              <xsl:otherwise>
                 <xsl:choose>
	            	<xsl:when test="rows/logo">
			           <xsl:value-of select="rows/logo"/>              
		             </xsl:when>
		             <xsl:otherwise>
		                <xsl:value-of select="concat($path,'/themes/default/img/logo3.svg')"/>
		             </xsl:otherwise>
           		</xsl:choose>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:variable>
          <div class="col-md-2 col-sm-3 sidebar tree-list" id="igrp-sidebar">

              <!-- NAV -->
                
        
            <div class="side-bar-ctrl visible-xs clearfix" >
                <i class="fa fa-navicon pull-right"></i>
            </div>
         
            <div class="igrp-sidebar-menu-search-wrapper igrp-sidebar-menu-search-container">
               <!-- Dark Logo-->
				    <a href="{rows/link}" class="logo logo-dark" style="display: flex;align-items: center;padding:  0 4px 0 20px;height: 60px;">
						<span class="logo-sm">
							<img src="{$logo}"  style="max-width: 80px;max-height: 55px;min-width: 60px;"/>
						</span>
						<div class="text-center"	style="color:white;font-size: 15px;font-weight: bold;line-height: normal;padding-left: 1rem;">
							<xsl:value-of select="rows/title" />
						</div>
                  </a>
              <i class="fa fa-search pull-left igrp-sidebar-menu-search-wrapper-icon"></i>
              <input 
            	id="menu_pesquisar"
                type="text" 
                class="form-control igrp-search-list" 
                placeholder="Pesquisar..." 
                search-list="#igrp-sidebar .treeview>a, #igrp-sidebar ul.treeview-menu>li>a"
                search-attr="text"
                search-item-parent=".treeview"
                search-item-wrapper=".treeview-menu" />
            </div>
            
            
    
            <ul class="nav nav-sidebar">
              <xsl:for-each select="$menus/menu">
                <xsl:variable name="parentId" select="concat($APP,'-',position())" />
                <li parent-id="{$parentId}">
                  <xsl:choose>
                    <xsl:when test="link">
                      <xsl:attribute name="class">treeview</xsl:attribute>                  
                      <a href="{link}" target="{submenu/target}" item-id="{$parentId}-{position()}" text="{title}" style=" align-items: center;">
                         <xsl:choose>
	                        <xsl:when test="submenu/menu_icon !=''" >
	                          <span class="menu-icon">
	                            <i class="{submenu/menu_icon}" />
	                          </span>
	                        </xsl:when> 
	                       <xsl:otherwise>
	                          <span class="menu-icon">
	                            <i class="fa fa-dot-circle-o" />
	                          </span>
	                        </xsl:otherwise> 
                        </xsl:choose>
                        <span class="menu-title">
                          <xsl:value-of select="title" />
                        </span>
                        <span class="nav-bar-active" bg-color="2" />                    
                      </a>                
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:if test="submenu">
                        <xsl:attribute name="class">treeview</xsl:attribute>
                      </xsl:if>                  
                      <a href="#" text="{title}" class="menu-title">
<!--                         <xsl:if test="menu_icon !=''" > -->
                         <span class="menu-icon">
                         	<i class="fa fa-list-ul"></i>
                         </span>
<!--                         </xsl:if>  -->
                        <xsl:value-of select="title" />
                        <i class="fa fa-angle-right pull-right"></i>
                        <span class="nav-bar-active" bg-color="2" />                    
                      </a>
                      <xsl:if test="submenu">
                        <ul class="treeview-menu">
                          <xsl:for-each select="submenu">
                           <xsl:sort select="order"/>
                            <li>
                              <a href="{link}"  target="{target}" item-id="{$parentId}-{position()}" text="{title}">
   							 <xsl:choose>
		                        <xsl:when test="menu_icon !=''" >
		                          <span class="menu-icon">
		                            <i class="{menu_icon}" />
		                          </span>
		                        </xsl:when> 
		                       <xsl:otherwise>
		                          <span class="menu-icon">
		                            <i class="fa fa-dot-circle-o" />
		                          </span>
		                        </xsl:otherwise> 
                      	    </xsl:choose>
                                <span class="menu-title">
                                  <xsl:value-of select="title" />
                                </span>                                                  
                              </a>
                            </li>
                          </xsl:for-each>
                        </ul>
                      </xsl:if>
                    </xsl:otherwise>
                  </xsl:choose>
                </li>
              </xsl:for-each>
            </ul>
    
          </div>
        </xsl:otherwise>
      </xsl:choose>
      
    </xsl:if>
  </xsl:template>
  <!-- BOTTOM -->
  <xsl:template name="IGRP-bottom">
  
    <xsl:apply-templates mode="form-hidden-fields" select="rows/content/hidden_form_igrp/fields"/>
  
    <!-- GLOBAL MODAL -->
    <div id="igrp-global-modal" class="modal fade" data-backdrop="static" tabindex="-1" role="dialog">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <span class="title"></span>
            <button type="button" class="close" data-dismiss="modal">×</button>
          </div>
          <div class="modal-body">
           
          </div>
          <div class="modal-footer">
            
          </div>
        </div>
      </div>
      <div style="display:none!important">
        <text id="confirm-text"><xsl:value-of select="rows/content/messages/message[@type='confirm']"/></text>
      </div>
    </div>
    <!--/GLOBAL MODAL -->


    <!-- GLOBAL MODAL ASIGNER -->
    <div id="igrp-global-modal-asigner" class="modal fade bd-example-modal-lg" data-backdrop="static" tabindex="-1" role="dialog">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <span class="title"></span>
            <button type="button" class="close">×</button>
          </div>
          <div class="modal-body">
           
          </div>
        </div>
      </div>
    </div>
    <!--/GLOBAL MODAL ASIGNER-->

    
    <!-- RIGHT PANEL MODAL -->
    <div class="modal fade right" id="igrp-right-panel">
      <div class="modal-dialog">
        <div class="igrp-right-panel-close" data-dismiss="modal">
          <i class="fa fa-times"></i>
        </div>
        <div class="modal-content">
          <iframe id="igrp-right-panel-iframe"/>
        </div>
        <div class="loader"/>
      </div>
    </div>
    <!--/RIGHT PANEL MODAL -->

    <igrp-variables class="hidden invisible">
      <igrp-page-title class="hidden"><xsl:value-of select="rows/content/title"/></igrp-page-title>
    </igrp-variables>

    <!-- local theme color setup -->
    <!-- <xsl:call-template name="local-theme-setup"/> -->

    <button class="btn btn-default" id="igrp-go-up" target="scroll_to_top" bg-color="primary">
      <i class="fa fa-arrow-up"></i>
    </button>

    <!-- IFRAME NAVIGATION MODAL -->
    <xsl:call-template name="iframe-nav"/>
    <!--/IFRAME NAVIGATION MODAL -->

    <!-- DEBUG -->
    <xsl:call-template name="IGRP-debug"/>
    <!--/DEBUG -->
    
    <!-- FOOTER -->
    <xsl:if test="rows/content/igrpfooter">
      <xsl:call-template name="IGRP-footer"/>
    </xsl:if>
  	<!-- /FOOTER -->
  	
     <!-- JS -->
    <script src="{$path}/core/bootstrap/{$bs-v}/js/bootstrap.min.js"></script>

    <xsl:if test="$themeConfigData/@version = '4'">
      <script type="module" src="https://unpkg.com/ionicons@5.5.2/dist/ionicons/ionicons.esm.js"></script>
      <script nomodule="" src="https://unpkg.com/ionicons@5.5.2/dist/ionicons/ionicons.js"></script>
    </xsl:if>
    
  </xsl:template>
  <!-- DEBUG -->
  <xsl:template name="IGRP-debug">
    <xsl:if test="rows/content/messages/message[@type='debug'] != ''">
      <xsl:variable name="contDebug" select="count(rows/content/messages/message[@type='debug'])"/>
      <a class="igrp-debug-ctrl">
        <i class="fa fa-chevron-up"></i>
      </a>
      <div class="modal fade " id="igrp-debugger" role="dialog" >
        <div class="modal-dialog modal-lg">
          <div class="modal-content">
            <div class="modal-header">
              <h3>Debugger</h3>
              <button type="button" class="close" data-dismiss="modal">×</button>
            </div>
            <div class="modal-body">
               <div class="igrp-debug-contents clearfix">
                  <xsl:if test="$contDebug &gt; 9">
                      <div class="igrp-debug-search form-group col-md-4 clearfix pull-right">
                          <input placeholder="Pesquisar" id="debug_pesquisar" type="text" class="not-form form-control"/>
                      </div>
                  </xsl:if>
                  <ul class="clear igrp-debug-list">
                      <xsl:for-each select="rows/content/messages/message[@type='debug']">
                        <li style="white-space: pre-wrap" value="{.}"><xsl:value-of select="." /></li>
                      </xsl:for-each>
                  </ul>
              </div>
            </div>

          </div>
        </div>
      </div>
    </xsl:if>
  </xsl:template>
  <!-- MESSAGES -->
  <xsl:template name="igrp-messages" match="messages" mode="igrp-messages">
    <xsl:param name="dismiss" select="'true'"/>
    <xsl:variable name="dismissClass">
      <xsl:if test="$dismiss = 'true' "><xsl:value-of select="alert-dismissible"/></xsl:if>
    </xsl:variable>
    <!--<xsl:if test="message">-->
      <div class="igrp-msg-wrapper gen-container-item">
        <xsl:for-each select="message[ not(@type='debug') and not(@type='confirm')]">
          
          <xsl:variable name="msg-type">
              <xsl:call-template name="igrp-msg-transform"/>
          </xsl:variable>

          <xsl:variable name="msg-icon">
              <xsl:call-template name="igrp-msg-icon"/>
          </xsl:variable>

          <div class="alert alert-{$msg-type} {$dismissClass}" role="alert">
            
            <i class="fa fa-{$msg-icon} igrp-msg-icon"></i>
            
            <xsl:if test="$dismiss = 'true'">
              <a class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true"><i class="fa fa-times"></i></span>
              </a>
            </xsl:if>
            <span class="disable-output-escaping px-4"><xsl:value-of select="." disable-output-escaping="yes"/></span>
          </div>

        </xsl:for-each>
      </div>
    <!--</xsl:if>-->
  </xsl:template>
  <!-- IFRAME NAV -->
  <xsl:template name="iframe-nav">
    <div class="modal fade" data-backdrop="static" tabindex="-1" id="igrp-iframe-nav" role="dialog" >
      
      <div class="iframe-nav-close"><i class="fa fa-close"></i></div>
      
      <div class="modal-dialog">
        <iframe></iframe>
      </div>

      <div class="loader"></div>

    </div>
  </xsl:template>
  <!-- COLOR PALLETES -->
  <xsl:template name="colorpalettes-css">
    <style>
      <xsl:for-each select="$palettesXML">
        .cp-<xsl:value-of select="@name"/> {
          background: <xsl:value-of select="."/>!important;
          border-color: <xsl:value-of select="."/>!important;
          color: <xsl:value-of select="@bg-text-color"/>!important;
        }
        .cp-<xsl:value-of select="@name"/>-text {
          color: <xsl:value-of select="."/>!important;
        }
      </xsl:for-each>
    </style>
  </xsl:template>
  <xsl:template name="colorpalettes-js">
    <script>
      $.IGRP.component('colorPalettes',{
        colors : [
        <xsl:for-each select="$palettesXML">
          {
            value:"cp-<xsl:value-of select="@name"/>",
            label:"<xsl:value-of select="@name"/>",
            color:"<xsl:value-of select="."/>",
            text :"<xsl:value-of select="@bg-text-color"/>"
          }<xsl:if test="position() != last()">,</xsl:if>
        </xsl:for-each>
        ]
      });
      

    </script>
  </xsl:template>
  <!-- COLORS Config -->
  <xsl:template name="theme-colors-config" mode="theme-colors-config" match="*">
    <style>
      
        <xsl:if test="nav">

          <xsl:if test="nav/background">
            #igrp-top-nav{
              background-color:<xsl:value-of select="nav/background"/>
            }
          </xsl:if>

          <xsl:if test="nav/brandColor">
           #side-bar-ctrl, 
           #igrp-app-title,
           .navbar-brand>span{
                color: <xsl:value-of select="nav/brandColor"/>;
           }
          </xsl:if>

          <xsl:if test="nav/buttonsColor">
           #igrp-top-menu>li>a, 
           #igrp-top-menu>li>a{
                color: <xsl:value-of select="nav/buttonsColor"/>;
           }
          </xsl:if>

          <xsl:if test="nav/buttonsHover">
           #igrp-top-menu>li>a:hover, 
           #igrp-top-menu>li>a:focus{
                color: <xsl:value-of select="nav/buttonsHover"/>;
           }
          </xsl:if>

        </xsl:if>

        <xsl:if test="sidebar">

          <xsl:if test="sidebar/@shadow = 'false'">
            #igrp-sidebar.sidebar{
            box-shadow:none;
            }
          </xsl:if>
          
          <xsl:if test="sidebar/background">
            #igrp-sidebar.sidebar{
              background-color:<xsl:value-of select="sidebar/background"/>!important
            }
          </xsl:if>

          <xsl:if test="sidebar/buttonsColor">
            #igrp-sidebar .nav-sidebar > li > a,
            .treeview-menu>li>a{
              color : <xsl:value-of select="sidebar/buttonsColor"/>
            }
          </xsl:if>

        </xsl:if>

        <xsl:if test="colors/color">
          <!--  -->
          a,.clickable,.btn-link{
            color:<xsl:value-of select="colors/color[@link='true']"/>;
          }
          <xsl:variable name="link-hover-color">
            <xsl:choose>
              <xsl:when test="colors/color[@link='true']/@bg-hover">
                <xsl:value-of select="colors/color[@link='true']/@bg-hover"/>
              </xsl:when>
              <xsl:otherwise>
                <xsl:value-of select="colors/color[@link='true']"/>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:variable>
          
          a:hover,
          .clickable:hover,
          a:focus,
          .clickable:focus,
          .btn-link:focus,
          .btn-link:hover{
            color:<xsl:value-of select="$link-hover-color"/>;
          }

          
          <xsl:for-each select="colors/color">

            * .bg-<xsl:value-of select="@name"/>{
              background-color:<xsl:value-of select="."/>!important;
            }

            * .text-<xsl:value-of select="@name"/>{
              color:<xsl:value-of select="."/>!important;
            }
            
            [bg-color="<xsl:value-of select="@name"/>"]{
              background-color:<xsl:value-of select="."/>!important;
              <xsl:if test="@text-color">
                color: <xsl:value-of select="@text-color"/>!important;
              </xsl:if>
            }

            [active-bg-color="<xsl:value-of select="@name"/>"].active{
              background-color:<xsl:value-of select="."/>!important;
              <xsl:if test="@text-color">
                color: <xsl:value-of select="@text-color"/>!important;
              </xsl:if>
            }

            [style-listener="true"].active [active-bg-color="<xsl:value-of select="@name"/>"]{
              background-color : <xsl:value-of select="."/>!important;
            }

            [border-color="<xsl:value-of select="@name"/>"]{
              border-color:<xsl:value-of select="."/>!important;
            }

            <xsl:if test="@bg-hover">
              [bg-hover="<xsl:value-of select="@name"/>"]:hover{
                background-color:<xsl:value-of select="@bg-hover"/>;
              }
            </xsl:if>
           
            [border-color="<xsl:value-of select="@name"/>"]{
              border-color:<xsl:value-of select="@border-hover"/>;
            }
            
            [text-color="<xsl:value-of select="@name"/>"]{
              color:<xsl:value-of select="."/>;
            }

            [active-text-color="<xsl:value-of select="@name"/>"].active,
            .active [active-text-color="<xsl:value-of select="@name"/>"]{
              color:<xsl:value-of select="."/>!important;
            }

            <!-- BUTTONS -->
            .btn-<xsl:value-of select="@name"/>,
            .bootstrap-iso .btn-<xsl:value-of select="@name"/>{
              <xsl:if test="@text-color">
                color:<xsl:value-of select="@text-color"/>;
              </xsl:if>
              background-color:<xsl:value-of select="."/>;
              <xsl:choose>
                <xsl:when test="@border-color">
                  border-color:<xsl:value-of select="@border-color"/>;
                </xsl:when>
                <xsl:otherwise>
                  border-color:transparent;
                </xsl:otherwise>
              </xsl:choose>

            }

            <xsl:if test="@border-hover">
              .btn-<xsl:value-of select="@name"/>:hover,
              .btn-<xsl:value-of select="@name"/>.focus, 
              .btn-<xsl:value-of select="@name"/>:focus,
              .btn-<xsl:value-of select="@name"/>:active,
              .btn-<xsl:value-of select="@name"/>:active:hover,
              
              .bootsrap-iso .btn-<xsl:value-of select="@name"/>:hover,
              .bootsrap-iso .btn-<xsl:value-of select="@name"/>.focus, 
              .bootsrap-iso .btn-<xsl:value-of select="@name"/>:focus,
              .bootsrap-iso .btn-<xsl:value-of select="@name"/>:active,
              .bootsrap-iso .btn-<xsl:value-of select="@name"/>:active:hover{
                border-color:<xsl:value-of select="@border-hover"/>
              }
            </xsl:if>

            <xsl:if test="@bg-hover">
              .btn-<xsl:value-of select="@name"/>:hover,
              .btn-<xsl:value-of select="@name"/>.focus, 
              .btn-<xsl:value-of select="@name"/>:focus,
              .btn-<xsl:value-of select="@name"/>:active,
              .btn-<xsl:value-of select="@name"/>:active:hover,
              
              .bootstrap-iso .btn-<xsl:value-of select="@name"/>:hover,
              .bootstrap-iso .btn-<xsl:value-of select="@name"/>.focus, 
              .bootstrap-iso .btn-<xsl:value-of select="@name"/>:focus,
              .bootstrap-iso .btn-<xsl:value-of select="@name"/>:active,
              .bootstrap-iso .btn-<xsl:value-of select="@name"/>:active:hover{
                background-color:<xsl:value-of select="@bg-hover"/>
              }
            </xsl:if>

            <xsl:if test="@text-hover">
              .btn-<xsl:value-of select="@name"/>:hover,
              .btn-<xsl:value-of select="@name"/>.focus, 
              .btn-<xsl:value-of select="@name"/>:focus,
              .btn-<xsl:value-of select="@name"/>:active,
              .btn-<xsl:value-of select="@name"/>:active:hover,
              
              .bootstrap-iso .btn-<xsl:value-of select="@name"/>:hover,
              .bootstrap-iso .btn-<xsl:value-of select="@name"/>.focus, 
              .bootstrap-iso .btn-<xsl:value-of select="@name"/>:focus,
              .bootstrap-iso .btn-<xsl:value-of select="@name"/>:active,
              .bootstrap-iso .btn-<xsl:value-of select="@name"/>:active:hover{
                color:<xsl:value-of select="@text-hover"/>
              }
            </xsl:if>

          </xsl:for-each>

          <xsl:if test="colors/color[@name='primary']">
            .checkbox-switch input:checked + .slider{
              background-color: <xsl:value-of select="colors/color[@name='primary']"/>!important;
            }
          </xsl:if>

          <xsl:if test="colors/color[@sidebar='true']">
             #igrp-sidebar.sidebar{
                 background-color: <xsl:value-of select="colors/color[@sidebar='true']"/>;
              }
          </xsl:if>

        </xsl:if>

        <xsl:variable name="mainColor">

          <xsl:choose>

            <xsl:when test="colors/color[@main='true']">
              <xsl:value-of select="colors/color[@main='true']"/>
            </xsl:when>

            <xsl:when test="sidebar/background">
               <xsl:value-of select="sidebar/background"/>
            </xsl:when>

            <xsl:otherwise>#337ab7</xsl:otherwise>

          </xsl:choose>

        </xsl:variable>


        .bg-primary,
        .bootstrap-iso .bg-primary{
          background-color:<xsl:value-of select="$mainColor"/>!important;
        }

        .text-primary,
        .bootstrap-iso  .text-primary{
          color:<xsl:value-of select="$mainColor"/>!important;
        }

        [active-text-color="primary"].active,
        .active [active-text-color="primary"]{
            color:<xsl:value-of select="$mainColor"/>!important;
        }

        <xsl:if test="not(colors/color)">
          [bg-color="primary"]{
            background-color:<xsl:value-of select="$mainColor"/>!important;
          }

          [active-bg-color="primary"].active{
            background-color:<xsl:value-of select="$mainColor"/>!important;
          }

          [style-listener="true"].active [active-bg-color="primary"]{
            background-color : <xsl:value-of select="$mainColor"/>!important;
          }
         
          [text-color="primary"]{
            color:<xsl:value-of select="$mainColor"/>;
          }

          [active-text-color="primary"].active,
          .active [active-text-color="primary"]{
            color:<xsl:value-of select="$mainColor"/>!important;
          }    
          [border-color="primary"]{
            border-color:<xsl:value-of select="$mainColor"/>!important;
          }

        </xsl:if>

        <xsl:if test="not(colors/color[@name='secondary']) or colors/color[@name='secondary'] =''">
          <xsl:variable name="secondaryColor" select="'#5b5b5b'"/>

          [bg-color="secondary"]{
            background-color:<xsl:value-of select="$secondaryColor"/>!important;
          }

          [active-bg-color="secondary"].active{
            background-color:<xsl:value-of select="$secondaryColor"/>!important;
          }

          [style-listener="true"].active [active-bg-color="secondary"]{
            background-color : <xsl:value-of select="$secondaryColor"/>!important;
          }
         
          [text-color="secondary"]{
            color:<xsl:value-of select="$secondaryColor"/>;
          }

          [active-text-color="secondary"].active,
          .active [active-text-color="secondary"]{
            color:<xsl:value-of select="$secondaryColor"/>!important;
          }    
          [border-color="secondary"]{
            border-color:<xsl:value-of select="$secondaryColor"/>!important;
          }

        </xsl:if>


		body.white-sidebar #igrp-sidebar.sidebar{
			background:white!important;
		}
		
    </style>
  </xsl:template>
  <!-- Local Themes definitions -->
  <xsl:template name="local-theme-setup" >
    
    <div id="igrp-local-theme">

      <div class="dropdown">

        <button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown">
          <span><i class="fa fa-gears"></i></span>
          <span class="caret"></span>
        </button>

        <div class="dropdown-menu">
          <div class="igrp-theme-part">
            <h5>Sidebar</h5>
            <ul class="clearfix">
              <xsl:for-each select="$palettesXML">
              	<li>
	                <a href="#"> <span class="igrp-theme-color" color="{.}" text-color="{@bg-text-color}" style="background:{.}"></span> </a>
	              </li>
              </xsl:for-each>
              <!-- <li>
                <a href="#"> <span class="igrp-theme-color white"></span> </a>
              </li>
              <li>
                <a href="#"> <span class="igrp-theme-color dark"></span> </a>
              </li> -->
            </ul>
          </div>

        </div>

      </div>
      
      <!-- <a class="igrp-local-theme-button">

        <i class="fa fa-gears"></i>

      </a>

      <ul class="igrp-local-theme-colors">

        <li>Red</li>

        <li>Orange</li>

      </ul> -->

    </div>

  </xsl:template>
  
  <!-- IGRP footer -->

  <xsl:template name="IGRP-footer">
    <footer class="IGRP-footer col-sm-9 col-md-10 col-md-offset-2 col-sm-offset-3">
      <xsl:if test="rows/content/igrpfooter/copyright">
        <div class="col-sm-8">
          <div class="disable-output-escaping">
            <xsl:value-of select="rows/content/igrpfooter/copyright" disable-output-escaping="yes"/>
          </div>
        </div>
      </xsl:if>
      <xsl:if test="rows/content/igrpfooter/developed">
        <div class="col-sm-4">
          <div class="text-right">
            <span class="disable-output-escaping mr-5">
              <xsl:value-of select="rows/content/igrpfooter/developed" disable-output-escaping="yes"/>
            </span>
            <a class="link disable-output-escaping" target="_newtab" href="{rows/content/igrpfooter/by/@link}">
              <xsl:value-of select="rows/content/igrpfooter/by" disable-output-escaping="yes"/>
            </a>
          </div>
        </div>
      </xsl:if>
    </footer>
  </xsl:template>
  
  <!--TEMPLATE FOR  NAVIGATION-->
  <xsl:include href="IGRP-navigation.tmpl.xsl?v=1"/>

  <xsl:include href="IGRP-v4-components.xsl?v=1"/>

</xsl:stylesheet>
