<?php
header('Content-Type: application/json');

$ruta_configuracion = "configuracion.php";

// Verificar si el archivo de configuración existe
if (!file_exists($ruta_configuracion)) {
    $respuesta = array('status' => 'ERROR', 'mensaje' => 'El archivo de configuración no existe');
    http_response_code(500); // 500 Internal Server Error
    echo json_encode($respuesta, JSON_UNESCAPED_UNICODE);
    exit();
}

// Incluir el archivo de configuración
require_once $ruta_configuracion;

// Verificar si la solicitud es de tipo GET
if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    
    $claveAcceso = $_GET['claveAcceso'];

    if (!$claveAcceso) {
        $respuesta = array('status' => 'ERROR', 'mensaje' => 'Falta el parámetro claveAcceso en la URL');
        http_response_code(400); // 400 Bad Request
        echo json_encode($respuesta, JSON_UNESCAPED_UNICODE);
        exit();
    }

    try {
        $dsn = "mysql:host=50.31.188.7;dbname=$base_de_datos;charset=UTF8";
        $opciones = array(
            PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION
        );
        $pdo = new PDO($dsn, $usuario, $contrasena, $opciones);

        
        // Consulta para obtener un token válido de la base de datos
        $consulta = "SELECT claveAcceso, rucEmisor, codigoBeneficio, baseImponible, porcentajeIva, montoIva FROM solicitudesDevolucionImpuesto WHERE claveAcceso = :claveAcceso";

        $stmt = $pdo->prepare($consulta);
        $stmt->bindParam(':claveAcceso', $claveAcceso);
        $stmt->execute();
        
        // Obtener los valores de la consulta
        $resultado = $stmt->fetch(PDO::FETCH_ASSOC);

        // Cerrar la conexión a la base de datos
        $pdo = null;

        if ($resultado) {
            // Respuesta satisfactoria con los campos solicitados
            $respuesta = array('status' => 'OK', 'payload' => $resultado);
            echo json_encode($respuesta, JSON_UNESCAPED_UNICODE);
        } else {
            $respuesta = array('status' => 'KO', 'mensaje' => 'No existe el registro. ');
            echo json_encode($respuesta, JSON_UNESCAPED_UNICODE);
        }

    } catch (PDOException $e) {
        // Manejo de errores PDO
        $respuesta = array('status' => 'ERROR', 'mensaje' => 'Error de PDO: ' . $e->getMessage());
        http_response_code(500); // 500 Internal Server Error
        echo json_encode($respuesta, JSON_UNESCAPED_UNICODE);
    } catch (Exception $e) {
        // Manejo de errores generales
        $respuesta = array('status' => 'ERROR', 'mensaje' => 'Error: ' . $e->getMessage());
        http_response_code(500); // 500 Internal Server Error
        echo json_encode($respuesta, JSON_UNESCAPED_UNICODE);
    }

} else {
    // La solicitud no es de tipo GET, enviar un error de método no permitido
    $respuesta = array('status' => 'ERROR', 'mensaje' => 'Método no permitido');
    http_response_code(405); // 405 Method Not Allowed
    echo json_encode($respuesta, JSON_UNESCAPED_UNICODE);
}
?>
