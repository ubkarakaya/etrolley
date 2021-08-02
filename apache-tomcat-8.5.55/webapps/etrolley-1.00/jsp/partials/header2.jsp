<header class="header2">
    <nav class="home-menu pure-menu pure-menu-horizontal pure-menu-fixed">
        <a class="pure-menu-heading" id="etrolley-header-brand" href="">e - T r o l l e y</a>

         <ul class="pure-menu-list">
                    <li class="pure-menu-item pure-menu-selected"><a href="home.jsp" class="pure-menu-link"> Home</a></li>
                    <li class="pure-menu-item pure-menu-selected"><a href="dashboard.jsp" class="pure-menu-link"> Dashboard</a></li>
                    <li class="pure-menu-item pure-menu-selected"><a href="createList.jsp" class="pure-menu-link"> Create List</a></li>
                    <li class="pure-menu-item pure-menu-selected"><a href="order-confirm.jsp" class="pure-menu-link">
                        <i class="fa fa-shopping-cart fa-lg"></i>
                        Go to Trolley</a></li>
                    <li class="pure-menu-item pure-menu-has-children pure-menu-allow-hover">
                        <a href="#" id="menuLink1" class="pure-menu-link">
                            <i class="fa fa-cog"></i>
                            Settings
                        </a>
                        <ul class="pure-menu-children">
                            <li class="pure-menu-item"><a href="profile.jsp" class="pure-menu-link">View Profile</a></li>
                        </ul>
                    </li>
                    <li class="pure-menu-item pure-menu-selected">
                        <form class="pure-form" method="get" action="supermarket-list.jsp">
                            <input type="text" class="pure-input-rounded" name="search" placeholder="Enter Supermarket..." required>
                            <button type="submit" class="pure-button pure-button-primary">Search</button>
                        </form>
                    </li>
                </ul>
    </nav>
</header>