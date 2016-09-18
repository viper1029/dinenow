import React, {Component} from 'react'
import {StyleSheet, Dimensions} from 'react-native'
import {Scene, Router, ActionConst} from 'react-native-router-flux'
import NavBar from './components/NavBar'
import NavigationDrawer from './components/NavigationDrawer'
import Theme from './styles/Theme'
import IntroScreen from './views/IntroScreen'
import SignInScreen from './views/SignInScreen'
import SearchScreen from './views/SearchScreen'
import AboutScreen from './views/AboutScreen'
import AccountScreen from './views/AccountScreen'
import ResultsScreen from './views/ResultsScreen'
import SendFeedbackScreen from './views/SendFeedbackScreen'
import RestaurantDetails from './views/RestaurantDetails'
import ItemScreen from './views/ItemScreen'
import {Actions as NavActions} from 'react-native-router-flux'

class NavigationRouter extends Component {

    render() {

        const DrawerNavBar = (props) => (<NavBar
            onLeftPress={() => {NavActions.refresh({key: 'drawer', open: value => !value })}}
            title={props.title}
            leftButton={true}
            leftIconName="menu"
            iconStyle={{
                fontSize: 32,
            }}
        />)

        const BackNavBar = (props) => (<NavBar
            onLeftPress={NavActions.pop}
            title={props.title}
            leftButton={true}
            iconStyle={{
                fontSize: 32,
            }}
        />)

        const BackToHomeNavBar = (props) => (<NavBar
            onLeftPress={NavActions.drawer}
            title={props.title}
            leftButton={true}
            iconStyle={{
                fontSize: 32,
            }}
        />)

        const BackNavBarNoShadow = (props) => (<NavBar
            onLeftPress={NavActions.pop}
            title={props.title}
            leftButton={true}
            iconStyle={{
                fontSize: 32,
            }}
            navBarStyle={{shadowRadius: 0,
                shadowOffset: {width: 0, height: 0},
                shadowOpacity: 0,
                elevation: 0,}}
        />)


        return (
            <Router>
                <Scene key='root'>

                    <Scene key="drawer" component={NavigationDrawer} open={false} type={ActionConst.RESET} direction="horizontal">
                        <Scene key="main" tabs={true}>
                            <Scene  key='search' component={SearchScreen} hideNavBar={false} navBar={() => (<DrawerNavBar title="Search"/>)}/>
                            <Scene key='about' component={AboutScreen} hideNavBar={false} navBar={() => (<DrawerNavBar title="About Us"/>)}/>
                            <Scene key='sendFeedback' component={SendFeedbackScreen} hideNavBar={false} navBar={() => (<DrawerNavBar title="Send Feedback"/>)}/>
                            <Scene key='account' component={AccountScreen} hideNavBar={false} navBar={() => (<DrawerNavBar title="Account"/>)}/>
                        </Scene>

                </Scene>
                    <Scene key='results' component={ResultsScreen} hideNavBar={false} navBar={() => (<BackToHomeNavBar title="Restaurants"/>)} />
                    <Scene key='restaurantDetails' component={RestaurantDetails} hideNavBar={false} navBar={() => (<BackNavBarNoShadow title="Restaurant Details"/>)} />
                    <Scene key='item' initial component={ItemScreen} hideNavBar={false} navBar={() => (<BackNavBar title="Item"/>)} />
                </Scene>
            </Router>
        )
    }
}

const styles = StyleSheet.create({
    navBar: {
        backgroundColor: Theme.toolbarDefaultBg,
        shadowColor: '#000',
        shadowOffset: {width: 0, height: 2},
        shadowOpacity: 0.1,
        shadowRadius: 1.5,
        elevation: 2
    }
})


export default NavigationRouter