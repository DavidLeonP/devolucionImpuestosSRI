<?php
header('Content-Type: application/json');

require_once "configuracion.php";


// Leer el contenido de la solicitud
$input = file_get_contents('php://input');
$data = json_decode($input, true);

// Verificar si los datos necesarios están presentes
if (!isset($data['DatosBeneficio'])) {
    echo json_encode([
        'codigo' => 'ERROR',
        'mensaje' => 'DatosBeneficio no proporcionado'
    ]);
    exit;
}

$datosBeneficio = $data['DatosBeneficio'];

$requiredFields = ['claveAccesoComprobante', 'estado', 'mensajeSolicitud'];
foreach ($requiredFields as $field) {
    if (!isset($datosBeneficio[$field])) {
        echo json_encode([
            'codigo' => 'ERROR',
            'mensaje' => "El campo $field es requerido"
        ]);
        exit;
    }
}

try {

    try {
        $pdo = new PDO("mysql:host=50.31.188.7;dbname=$base_de_datos", $usuario, $contrasena);
        $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    } catch (PDOException $e) {
        error_log('Error de conexión: ' . $e->getMessage());
        echo json_encode([
            'codigo' => 'ERROR',
            'mensaje' => 'Error de conexión a la base de datos'
        ]);
        exit;
    }


    $stmt = $pdo->prepare("UPDATE solicitudesDevolucionImpuesto SET estado = :estado , mensajeSolicitud = :mensajeSolicitud WHERE claveAcceso = :claveAccesoComprobante");

    // Vincular los valores de los parámetros
    $stmt->bindParam(':estado', $datosBeneficio['estado']);
    $stmt->bindParam(':mensajeSolicitud', $datosBeneficio['mensajeSolicitud']);
    $stmt->bindParam(':claveAccesoComprobante', $datosBeneficio['claveAccesoComprobante']);

    // Ejecutar la declaración SQL
    $stmt->execute();

    // Verificar si se realizó la actualización correctamente
    if ($stmt->rowCount() > 0) {
        // Construir la respuesta
        $response = [
            'codigo' => 'OK',
            'mensaje' => 'Se ha actualizado correctamente el estado del registro.'
        ];
    } else {
        // Construir la respuesta
        $response = [
            'codigo' => 'ERROR',
            'mensaje' => 'No se encontró ningún registro con la clave de acceso proporcionada.'
        ];
    }


    // Devolver la respuesta como JSON
    echo json_encode($response);
} catch (PDOException $e) {
    echo json_encode([
        'codigo' => 'ERROR',
        'mensaje' => 'Error al insertar los datos en la base de datos: ' . $e->getMessage()
    ]);
}
?>
