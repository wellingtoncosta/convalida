package convalida.sample.robot

/**
 * @author Wellington Costa on 26/06/2019.
 */

private typealias Robot = ConvalidaRobot

private typealias RobotBuilder = Robot.() -> Unit

private typealias TypeText = ConvalidaRobot.TypeText

private typealias Result = ConvalidaRobot.Result

private typealias ResultBuilder = Result.() -> Unit

object ConvalidaRobotExtension {

    fun convalida(builder: RobotBuilder) = Robot().apply(builder)

    val Robot.name: TypeText get() = this.name()

    val Robot.nickName: TypeText get() = this.nickName()

    val Robot.age: TypeText get() = this.age()

    val Robot.phone: TypeText get() = this.phone()

    val Robot.cpf: TypeText get() = this.cpf()

    val Robot.cnpj: TypeText get() = this.cnpj()

    val Robot.isbn: TypeText get() = this.isbn()

    val Robot.email: TypeText get() = this.email()

    val Robot.confirmEmail: TypeText get() = this.confirmEmail()

    val Robot.password: TypeText get() = this.password()

    val Robot.confirmPassword: TypeText get() = this.confirmPassword()

    val Robot.creditCard: TypeText get() = this.creditCard()

    val Robot.numericLimit: TypeText get() = this.numericLimit()

    val Robot.ipv4: TypeText get() = this.ipv4()

    val Robot.ipv6: TypeText get() = this.ipv6()

    val Robot.url: TypeText get() = this.url()

    val Robot.date: TypeText get() = this.date()

    infix fun TypeText.typeText(text: String): Robot = this.typeText(text)

    infix fun Robot.results(builder: ResultBuilder) = Result().apply(builder)

    infix fun Robot.validates(builder: ResultBuilder): ConvalidaRobot =
            this.validate().also { Result().apply(builder) }

    infix fun Robot.clears(builder: ResultBuilder): ConvalidaRobot =
            this.clear().also { Result().apply(builder) }

}
