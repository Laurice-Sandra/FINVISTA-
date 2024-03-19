<!-- src/main/resources/templates/loan-rejection.ftl -->
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Loan Application Update</title>
    <style>
        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
        .container { width: 80%; margin: auto; padding: 20px; }
        .header { background-color: #4CAF50; padding: 20px; color: white; text-align: center; }
        .content { margin-top: 20px; background-color: #f8f8f8; padding: 20px; }
        .footer { margin-top: 20px; padding: 20px; background-color: #f4f4f4; text-align: center; }
        a.button { background-color: #f44336; color: white; padding: 10px 20px; text-align: center; text-decoration: none; display: inline-block; }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h2>Your Loan Application Status</h2>
    </div>
    <div class="content">
        <p>Dear ${user.firstName},</p>
        <p>We regret to inform you that after careful consideration, your application for a loan has been declined at this time.</p>
        <p><strong>Reason:</strong> ${reason}</p>
        <p>We understand this may be disappointing, but we would like to encourage you not to lose hope. Here are a few suggestions:</p>
        <ul>
            <li>Review your financial situation and consider if there are any changes you could make to improve your application.</li>
            <li>Consider applying again in the future when your circumstances may have changed.</li>
            <li>If you have any questions or would like advice on improving your application, please feel free to contact us.</li>
        </ul>
        <p>We appreciate your interest in FinvistaFlexiFin and hope we can assist you in the future.</p>
        <p>Warm regards,</p>
        <p>Your FinvistaFlexiFin Team</p>
    </div>
    <div class="footer">
        <p>This is an automated message, please do not reply directly to this email.</p>
    </div>
</div>
</body>
</html>
